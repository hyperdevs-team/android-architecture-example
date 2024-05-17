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

package com.hyperdevs.arch_example.crashreporting

import mini.Reducer
import mini.State
import mini.Store
import mini.kodein.bindStore
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

/**
 * Crash reporting state. Empty since it's mandatory.
 * We need it to be a data class with any value so the compilation doesn't crash with proguard since using the android
 * gradle plugin 4.2.0.
 */
data class CrashReportingState(private val empty: String? = null): State

/**
 * Store that handles reporting data to the crash reporting service.
 */
@Suppress("UndocumentedPublicFunction")
class CrashReportingStore(private val controller: CrashReportingController) : Store<CrashReportingState>() {

    @Reducer
    fun onLogHandledCrash(action: LogHandledCrashAction) {
        controller.logException(action.throwable)
    }

    @Reducer
    fun onLogMessage(action: LogCrashReportingMessageAction) {
        controller.log(action.message)
    }

    @Reducer
    fun onIncludeCrashReportingUserData(action: AddCrashReportingUserDataAction) {
        controller.setUserId(
            userId = action.userIdentifier
        )
    }

    @Reducer
    fun onAddCrashReportingKeys(action: AddCrashReportingKeysAction) {
        action.keyMap?.let { controller.setProperties(it) }
    }
}

@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
object CrashReportingModule {
    fun create() = DI.Module("CrashReportingModule") {
        bindStore { CrashReportingStore(instance()) }
        bind<CrashReportingController>() with singleton { CrashReportingControllerImpl(instance()) }
        bind<CrashReportingManager>() with singleton { CrashReportingManagerImpl(instance()) }

        bind<CrashReportingLogMiddleware>() with singleton { CrashReportingLogMiddleware(instance()) }
    }
}