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

import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

/**
 * Interface that a crash reporting tool should provide.
 */
interface CrashReportingManager {
    /**
     * Set custom properties for crash reports.
     */
    fun setProperties(keyMap: Map<String, Any>)

    /**
     * Sets the user id for crash reports.
     */
    fun setUserId(userId: String?)

    /**
     * Logs an exception.
     */
    fun logException(throwable: Throwable)

    /**
     * Logs a custom message associated to the current crash reporting session.
     */
    fun log(msg: String)
}

/**
 * Crashlytics implementation of [CrashReportingManager].
 */
class CrashReportingManagerImpl(private val crashlytics: FirebaseCrashlytics) : CrashReportingManager {
    override fun setProperties(keyMap: Map<String, Any>) {
        keyMap.forEach { (key, value) ->
            when (value) {
                is Float -> crashlytics.setCustomKey(key, value)
                is Boolean -> crashlytics.setCustomKey(key, value)
                is Double -> crashlytics.setCustomKey(key, value)
                is String -> crashlytics.setCustomKey(key, value)
                is Int -> crashlytics.setCustomKey(key, value)
                else -> Timber.e("The value $value with key $key is not an accepted value for Crashlytics")
            }
        }
    }

    override fun setUserId(userId: String?) {
        userId?.let { crashlytics.setUserId(userId) }
    }

    override fun logException(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    override fun log(msg: String) {
        crashlytics.log(msg)
    }
}