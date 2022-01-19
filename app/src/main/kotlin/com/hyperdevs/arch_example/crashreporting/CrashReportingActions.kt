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

import mini.Action
import mini.SilentAction

/**
 * Action used to log handled actions across the application code.
 */
data class LogHandledCrashAction(val throwable: Throwable) : SilentAction

/**
 * Action used to log messages in the crash reporting tool.
 */
@Action
data class LogCrashReportingMessageAction(val message: String)

/**
 * Generic action used to set the user identifiers on the crash reporting service.
 * Also remember that every class that implements this action MUST also inherit from [Action].
 */
@Action
interface AddCrashReportingUserDataAction {
    val userIdentifier: String?
}

/**
 * Generic action to add a key-value map to the current crash reporting instance.
 */
interface AddCrashReportingKeysAction : SilentAction {
    val keyMap: Map<String, Any>?
}