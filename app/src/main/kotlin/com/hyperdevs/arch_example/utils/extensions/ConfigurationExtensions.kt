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

@file:Suppress("MagicNumber")

package com.hyperdevs.arch_example.utils.extensions

import android.content.res.Configuration

/**
 * Returns true if this device has a big screen such as tablets, both small or large ones.
 */
fun Configuration.isTablet() = smallestScreenWidthDp >= 600

/**
 * Returns true if this device is a large tablet (tablet of 10" onwards).
 */
fun Configuration.isLargeTablet() = smallestScreenWidthDp >= 720

/**
 * Returns true if this device is a small tablet (tablet of 7" onwards until 10").
 */
fun Configuration.isSmallTablet() = isTablet() && !isLargeTablet()