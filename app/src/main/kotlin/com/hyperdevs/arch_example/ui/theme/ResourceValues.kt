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

@file:Suppress("ConstructorParameterNaming")
package com.hyperdevs.arch_example.ui.theme

/**
 * Class in charge to store different resources values such as Int, or Float.
 */
data class ResourceValues(
    // General
    val general_grid_cells: Int = 1,
    val general_header_elevation_gradient_alpha: Float = 0.22f,
    val general_image_gradient_overlay_alpha: Float = 0.6f,
    val alpha_max_in_integer_scale: Int = 255, // 100% in 0-255 scale
    // Characters
)

// Phone dimensions are the default ones
val phoneResourceValues = ResourceValues()

// Specific values for small tablets
val smallTabletResourceValues = ResourceValues(
    general_grid_cells = 2,
)

// Specific values for large tablets
val largeTabletResourceValues = smallTabletResourceValues.copy(
    general_grid_cells = 4,
)
