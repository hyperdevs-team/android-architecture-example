/*
 *
 *  * Copyright 2021 HyperDevs
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.hyperdevs.arch_example

import android.app.Application
import android.content.Context
import androidx.annotation.RestrictTo
import androidx.lifecycle.ViewModelProvider
import com.hyperdevs.arch_example.characters.DisneyModule
import com.hyperdevs.arch_example.characters.ui.CharacterDetailsViewModel
import com.hyperdevs.arch_example.characters.ui.CharactersViewModel
import com.hyperdevs.arch_example.crashreporting.PriorityUncaughtExceptionHandler
import com.hyperdevs.arch_example.crashreporting.toPriorityUncaughtExceptionHandler
import com.hyperdevs.arch_example.network.NetworkModule
import mini.Dispatcher
import mini.LoggerMiddleware
import mini.Mini
import mini.Store
import mini.kodein.android.DIViewModelFactory
import mini.kodein.android.TypedViewModelFactory
import mini.kodein.android.bindViewModel
import mini.kodein.android.bindViewModelFactory
import org.kodein.di.*
import org.kodein.di.conf.ConfigurableDI
import timber.log.Timber
import java.io.Closeable
import java.util.*
import kotlin.properties.Delegates

private var appInstance: App by Delegates.notNull()
val app: App get() = appInstance

/**
 * Base [Application] object used in the app.
 */
open class App : Application(), DIAware {

    override val di = ConfigurableDI(mutable = true)

    private lateinit var dispatcher: Dispatcher
    private lateinit var stores: List<Store<*>>
    private lateinit var storeSubscriptions: Closeable

    /**
     * Available exception handlers. We're using a TreeSet here to order exception handlers by
     * priority, from most prioritary (0) to least prioritary (100).
     *
     * This is like this because we're using the natural ordering of Ints (by using compareTo).
     */
    private val exceptionHandlers: TreeSet<PriorityUncaughtExceptionHandler> = TreeSet()

    companion object {
        const val KODEIN_APP_TAG = "AppTag"
    }

    override fun onCreate() {
        appInstance = this
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        initializeInjection(shouldInitializeStores())
//        initializeExceptionHandling()
    }

    /**
     * Initializes dependency injection.
     */
    private fun initializeInjection(initializeStores: Boolean) {
        if (this::storeSubscriptions.isInitialized) {
            storeSubscriptions.close()
        }

        if (this::stores.isInitialized) {
            stores.forEach { it.close() }
        }

        di.clear()

        di.addImports(false,
            // Domain
            AppModule.create(),
            DisneyModule.create(),
            // UI
            ViewModelsModule.create(),
            // Network
            NetworkModule.create(),
//            ConnectivityModule.create(),
//            FirebaseModule.create(),
            // Misc
//            CrashReportingModule.create(), // Remember to add the google services json if this module is added
        )

        stores = di.direct.instance<Set<Store<*>>>().toList()
        dispatcher = di.direct.instance()

        dispatcher.apply {
//            addMiddleware(di.direct.instance<CrashReportingLogMiddleware>())
            addMiddleware(
                LoggerMiddleware(stores, logger = { priority, tag, msg ->
                    Timber.tag(tag).d("[$priority] $msg")
                })
            )
        }

        storeSubscriptions = Mini.link(dispatcher, stores)
        if (initializeStores) {
            stores.forEach { store ->
                store.initialize()
            }
        }
    }

    /**
     * Whether or not stores should be initialized.
     *
     * Only useful in UI tests.
     */
    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    open fun shouldInitializeStores() = true

    /**
     * Adds an exception handler for the app.
     */
    fun addExceptionHandler(exceptionHandler: PriorityUncaughtExceptionHandler) {
        exceptionHandlers.add(exceptionHandler)
    }

    private fun initializeExceptionHandling() {
        val exceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
            ?.toPriorityUncaughtExceptionHandler("default")
        exceptionHandler?.let { addExceptionHandler(it) }
        Thread.setDefaultUncaughtExceptionHandler { thread, error ->
            exceptionHandlers.forEach {
                Timber.d("Launching exception handler with ID: ${it.id}")
                it.uncaughtException(thread, error)
            }
        }
    }
}

/**
 * Kodein module that provides app dependencies.
 */
object AppModule {
    @Suppress("UndocumentedPublicFunction")
    fun create() = DI.Module("AppModule", true) {
        bind<Application>(App.KODEIN_APP_TAG) with singleton { app }
        bind<Context>() with singleton { app }
        bind<Dispatcher>() with singleton { Dispatcher() }

        bind<ViewModelProvider.Factory>() with singleton { DIViewModelFactory(di.direct) }

        bindSet<Store<*>>()
    }
}

/**
 * Kodein module for View Models.
 */
object ViewModelsModule {
    @Suppress("UndocumentedPublicFunction")
    fun create() = DI.Module("HomeViewModelsModule", true) {
        bindViewModel { CharactersViewModel(instance(App.KODEIN_APP_TAG)) }
        bindViewModelFactory<CharacterDetailsViewModel, ViewModelProvider.Factory> { characterId ->
            TypedViewModelFactory(
                CharacterDetailsViewModel::class,
                instance(App.KODEIN_APP_TAG),
                characterId as Int
            )
        }
    }
}

private fun ConfigurableDI.addImports(allowOverride: Boolean, vararg moduleInfo: DI.Module) {
    moduleInfo.forEach { addImport(it, allowOverride) }
}