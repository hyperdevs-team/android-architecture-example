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

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.hyperdevs.arch_example.navigation.ScreenType

/**
 * Navigate to the start destination of the given [ScreenType].
 */
fun NavController.navigateToStartDestination(screenType: ScreenType) {
    navigate(screenType.route) {
        // Pop up to the start destination of the graph to avoid building up a large stack of
        // destinations on the back stack as users select items
        popUpTo(graph.findStartDestination().id) {
            saveState = false
        }
        // Avoid multiple copies of the same destination when reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = false
    }
}

/**
 * Navigate to the start destination of the given route.
 */
fun NavController.navigateToStartDestination(route: String) {
    navigate(route) {
        // Pop up to the start destination of the graph to avoid building up a large stack of
        // destinations on the back stack as users select items
        popUpTo(graph.findStartDestination().id) {
            saveState = false
        }
        // Avoid multiple copies of the same destination when reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = false
    }
}