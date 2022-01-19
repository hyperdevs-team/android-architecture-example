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

package com.hyperdevs.arch_example.utils

import mini.*

/**
 * ViewData that exposes a [Resource] based on the state of a [Task] or list of [Task]s.
 */
@Suppress("UndocumentedPublicFunction")
object ResponseViewData {
    fun from(task: Task): Resource<ResponseViewData> = from(listOf(task))

    fun from(tasks: List<Task>): Resource<ResponseViewData> = with(tasks) {
        when {
            allSuccessful() -> Resource.success(ResponseViewData)
            anyFailure() -> Resource.failure(tasks.mapNotNull { it.exceptionOrNull() }.firstOrNull())
            anyLoading() -> Resource.loading()
            else -> Resource.empty()
        }
    }
}