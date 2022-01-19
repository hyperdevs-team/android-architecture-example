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

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.hyperdevs.arch_example.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mini.Dispatcher
import mini.flow
import mini.select
import org.kodein.di.instance

/**
 * [ViewModel] used for things related to connectivity.
 */
class ConnectivityViewModel(app: Application) : BaseViewModel(app) {

    private val dispatcher: Dispatcher by instance()
    private val connectivityStore: ConnectivityStore by instance()
    private var listenToConnectivityChangesJob: Job? = null

    private val _isConnectionAvailable = MutableStateFlow(true)
    val isConnectionAvailable: StateFlow<Boolean> get() = _isConnectionAvailable

    /**
     * Start listening to connectivity changes.
     */
    fun startListeningToConnectivityChanges() {
        listenToConnectivityChangesJob?.cancel()
        listenToConnectivityChangesJob = connectivityStore.flow()
            .select { it.hasInternetConnection }
            .onEach {
                _isConnectionAvailable.value = it
            }.launchIn(viewModelScope)

        viewModelScope.launch {
            dispatcher.dispatch(StartListeningConnectivityChangesAction)
        }
    }

    /**
     * Stop listening to connectivity changes.
     */
    fun stopListeningToConnectivityChanges() {
        listenToConnectivityChangesJob?.cancel()

        viewModelScope.launch {
            dispatcher.dispatch(StopListeningConnectivityChangesAction)
        }

    }
}
