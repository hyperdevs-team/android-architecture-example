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

package com.hyperdevs.arch_example.ui.components.commons

import androidx.compose.material.BottomNavigationDefaults
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * [DisposableEffect] in charge of showing dark or light icons in the system bars and restore them when
 * disposing the Composable.
 */
@Composable
fun StatusBarDisposableEffect(darkIcons: Boolean = true) {
    val systemUiController = rememberSystemUiController()
    val oldStatusBarColor = remember { systemUiController.statusBarDarkContentEnabled }
    val oldNavigationBarColor = remember { systemUiController.navigationBarDarkContentEnabled }
    val backgroundColor = MaterialTheme.colors.background
    val elevatedBackgroundColor = LocalElevationOverlay.current?.apply(
        color = backgroundColor, elevation = BottomNavigationDefaults.Elevation
    )
    DisposableEffect(Unit) {
        systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = darkIcons)

        onDispose {
            systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = oldStatusBarColor)

            systemUiController.setNavigationBarColor(
                color = elevatedBackgroundColor ?: backgroundColor,
                darkIcons = oldNavigationBarColor
            )
        }
    }
}