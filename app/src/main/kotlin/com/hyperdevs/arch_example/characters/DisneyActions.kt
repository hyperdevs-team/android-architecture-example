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

package com.hyperdevs.arch_example.characters

import com.hyperdevs.arch_example.network.models.DisneyCharacterId
import mini.Action

/**
 * Triggered for getting the list of Disney characters.
 */
@Action
object GetDisneyCharactersAction

/**
 * Triggered for getting the details of a Disney character given its id.
 */
@Action
data class GetDisneyCharacterDetailsAction(val characterId: DisneyCharacterId)

/**
 * Triggered for logging when a task is set to [Task.loading()]] in a suspend [@Reducer] in a [Store].
 * This action won't change anything in the state, it is used only for getting info about when the process has started,
 * as if not, only the final state will be logged in the [LoggerMiddleware].
 */
@Action
data class OnTaskLoadingAction(val task: String)