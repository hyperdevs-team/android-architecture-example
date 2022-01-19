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

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter
import coil.size.Scale

/**
 * Load an image from the given [imageUrl] and crop it if needed.
 */
@Composable
fun ImageFromUrl(imageUrl: String,
                 modifier: Modifier = Modifier,
                 @DrawableRes placeholderRes: Int? = null,
                 contentDescription: String? = null,
                 colorFilter: ColorFilter? = null,
                 contentScale: ContentScale = ContentScale.Crop,
                 enableCrossfade: Boolean = false) {
    Image(
        modifier = modifier,
        contentScale = contentScale,
        painter = rememberImagePainter(
            data = imageUrl,
            builder = {
                scale(Scale.FILL)
                crossfade(enableCrossfade)
                placeholderRes?.let { placeholder(it) }
            }
        ),
        contentDescription = contentDescription,
        colorFilter = colorFilter,
    )
}