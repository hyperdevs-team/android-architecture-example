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

package com.hyperdevs.arch_example.navigation

/**
 * All different kinds of screens we can navigate to in the [NavigationGraph].
 */
sealed interface ScreenType {
    val route: String

    /**
     * The route definition will be different from [route] in any screen with arguments, either optional or mandatory.
     * Override this variable with the actual value of the route definition for those screens.
     * The [routeDefinition] is what is used in the [NavHost] to declare the definition of the route to navigate to a
     * screen with the key string used for the different arguments, and [route] is the actual route you will need to
     * navigate to that screen plus the required arguments' values if any.
     */
    val routeDefinition
        get() = route
}