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

/**
 * Controller interface that a class must match in order to provide crash reporting functionality.
 */
interface CrashReportingController {

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

@SuppressWarnings("UndocumentedPublicClass")
class CrashReportingControllerImpl(private val manager: CrashReportingManager) : CrashReportingController {
    override fun setProperties(keyMap: Map<String, Any>) {
        manager.setProperties(keyMap)
    }

    override fun setUserId(userId: String?) {
        manager.setUserId(userId)
    }

    override fun logException(throwable: Throwable) {
        manager.logException(throwable)
    }

    override fun log(msg: String) {
        manager.log(msg)
    }
}