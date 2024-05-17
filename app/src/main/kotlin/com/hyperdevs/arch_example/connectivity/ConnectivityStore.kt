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

package com.hyperdevs.arch_example.connectivity

import mini.Reducer
import mini.State
import mini.Store
import mini.Task
import mini.kodein.bindStore
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

@Suppress("UndocumentedPublicClass")
data class ConnectivityState(val hasInternetConnection: Boolean = true,
                             val listenToConnectivityChangesTask: Task = Task.idle()): State

/**
 * Store that manages the Internet connection status.
 */
@Suppress("UndocumentedPublicFunction")
class ConnectivityStore(private val connectivityController: ConnectivityController) : Store<ConnectivityState>() {

    @Reducer
    fun startListeningConnectivityChanges(state: ConnectivityState,
                                          action: StartListeningConnectivityChangesAction) =
        state.copy(
            listenToConnectivityChangesTask = connectivityController.startListeningConnectivityChanges(),
            hasInternetConnection = connectivityController.hasInternetConnection()
        )

    @Reducer
    fun stopListeningConnectivityChanges(state: ConnectivityState,
                                         action: StopListeningConnectivityChangesAction): ConnectivityState {
        connectivityController.stopListeningConnectivityChanges()
        return state.copy(listenToConnectivityChangesTask = Task.idle())
    }

    @Reducer
    fun onDeviceConnectedToTheInternet(state: ConnectivityState, action: DeviceConnectedToTheInternetAction) =
        state.copy(hasInternetConnection = true)

    @Reducer
    fun onDeviceDisconnectedFromTheInternet(state: ConnectivityState, action: DeviceDisconnectedFromTheInternetAction) =
        state.copy(hasInternetConnection = false)
}

@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
object ConnectivityModule {
    fun create() = DI.Module("ConnectivityStoreModule") {
        bindStore { ConnectivityStore(instance()) }
        bind<ConnectivityController>() with singleton {
            ConnectivityControllerImpl(instance(), instance())
        }
    }
}
