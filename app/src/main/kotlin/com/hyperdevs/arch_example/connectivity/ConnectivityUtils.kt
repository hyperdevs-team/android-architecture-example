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
import android.net.NetworkCapabilities
import android.os.Build
import com.hyperdevs.arch_example.utils.IllegalStateAppException

/**
 * [Exception] thrown when the user hasn't internet connection.
 */
object NetworkUnavailableException : IllegalStateAppException("There is no Internet connection")

/**
 * Utils related to the Internet connection.
 */
object ConnectivityUtils {

    /**
     * Checks if the device has Internet connection.
     */
    fun hasInternetConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            // TODO Check connection in captive portals, as we may not have internet as even this method return that we have
            capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo // Deprecated in 29
            activeNetwork != null && activeNetwork.isConnected // // Deprecated in 28
        }
    }

    /**
     * Checks if the device has Internet connection of mobile/cellular type.
     */
    fun hasMobileConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            // TODO Check connection in captive portals, as we may not have internet as even this method return that we have
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo // Deprecated in 29
            // Deprecated in 28
            networkInfo != null && networkInfo.isConnected
            && (networkInfo.type == ConnectivityManager.TYPE_MOBILE
                || networkInfo.type == ConnectivityManager.TYPE_MOBILE_DUN
                || networkInfo.type == ConnectivityManager.TYPE_MOBILE_HIPRI
                || networkInfo.type == ConnectivityManager.TYPE_MOBILE_MMS
                || networkInfo.type == ConnectivityManager.TYPE_MOBILE_SUPL)
        }
    }
}