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

import mini.Resource

/**
 * Generic data to manage dialogs in which the user has to enter some kind of value, such as in a [TextField] or checkbox, etc.
 * Also, that manages different UI cases depending on the state of the related task through the [resource] param.
 */
data class DialogViewData<T, R>(val showDialog: Boolean = false,
                                val value: T? = null,
                                val resource: Resource<R> = Resource.idle())