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

package com.hyperdevs.arch_example.utils.extensions

/**
 * Format an int as two digits, so for example 1 will be "01" and 15 will remain as "15".
 */
@Suppress("ImplicitDefaultLocale")
fun Int.formatAsTwoDigits() = String.format("%02d", this)

@Suppress("UndocumentedPublicFunction")
fun Int.floorMod(other: Int): Int =
    if (other == 0) this
    else this - floorDiv(other) * other