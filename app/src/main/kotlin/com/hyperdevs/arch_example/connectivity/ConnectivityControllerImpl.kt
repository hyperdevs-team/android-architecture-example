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

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.*
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import mini.Dispatcher
import mini.Task
import timber.log.Timber
import java.util.*

/**
 * Implementation for [ConnectivityController].
 */
class ConnectivityControllerImpl(private val context: Context,
                                 private val dispatcher: Dispatcher) : ConnectivityController {

    private val scope = CoroutineScope(SupervisorJob())

    /**
     * [onAvailable] and [onLost] may be called several times with different types of networks (wifi, cellular, etc),
     * so we need to track which types are still available when the [onLost] callback is called.
     * For example in devices with both cellular and wifi, after enabling the airplane mode and disabling it, it will
     * connect first to the cellular network and after to the wifi one, receiving that the cellular one has been lost,
     * meanwhile the wifi one would be still available.
     */
    private val availableNetworks = Collections.synchronizedSet(mutableSetOf<Network>())

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)

            val notNetworkWasAvailable = availableNetworks.isEmpty()
            availableNetworks.add(network)
            if (notNetworkWasAvailable) {
                scope.launch {
                    dispatcher.dispatch(DeviceConnectedToTheInternetAction)
                }
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)

            // The equals of a [Network] is done with the internal [netId], not with the whole network object, so it is
            // secure to do it this way
            availableNetworks.remove(network)
            if (availableNetworks.isEmpty()) {
                scope.launch {
                    dispatcher.dispatch(DeviceDisconnectedFromTheInternetAction)
                }
            }
        }

        // TODO: Not working right now: https://issuetracker.google.com/issues/144891976. Check in the future
        override fun onUnavailable() {
            super.onUnavailable()
            if (availableNetworks.isEmpty()) {
                scope.launch {
                    dispatcher.dispatch(DeviceDisconnectedFromTheInternetAction)
                }
            }
        }
    }

    override fun startListeningConnectivityChanges(): Task =
        try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val request = NetworkRequest.Builder().apply {
                addCapability(NET_CAPABILITY_NOT_RESTRICTED)
                addCapability(NET_CAPABILITY_INTERNET)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    addCapability(NET_CAPABILITY_VALIDATED)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    addCapability(NET_CAPABILITY_FOREGROUND)
                    addCapability(NET_CAPABILITY_NOT_SUSPENDED)
                }
            }.build()

            connectivityManager.registerNetworkCallback(request, networkCallback)
            Task.success()
        } catch (e: Throwable) {
            Task.failure(e)
        }

    override fun stopListeningConnectivityChanges() {
        try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.unregisterNetworkCallback(networkCallback)
            availableNetworks.clear()
        } catch (e: Throwable) {
            Timber.e(e, "Error unregistering the network callback")
        }
    }

    override fun hasInternetConnection() = ConnectivityUtils.hasInternetConnection(context)
}