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

import mini.Chain
import mini.Middleware

/**
 * Interceptor used to log the flow of actions into the Crash Reporting logs.
 */
class CrashReportingLogMiddleware(private val crashReportingManager: CrashReportingManager) : Middleware {

    override suspend fun intercept(action: Any, chain: Chain): Any {
        crashReportingManager.log(action.toString())
        return chain.proceed(action)
    }
}