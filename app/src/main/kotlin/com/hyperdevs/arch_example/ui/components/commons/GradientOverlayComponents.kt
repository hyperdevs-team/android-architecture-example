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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ContentAlpha
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

/**
 * Image of the given [imageUrl] with a gradient overlay in the orientation given in [gradientOverlayOrientation].
 */
@Composable
fun ImageWithGradientOverlay(imageUrl: String,
                             modifier: Modifier = Modifier,
                             contentDescription: String? = null,
                             contentScale: ContentScale = ContentScale.Crop,
                             enableCrossfade: Boolean = true,
                             @DrawableRes placeholderRes: Int,
                             gradientOverlayOrientation: GradientOverlayOrientation =
                                 GradientOverlayOrientation.BOTTOM_TO_TOP,
                             gradientColors: List<Color>) {

    GradientOverlayContainer(GradientBrush.from(gradientColors, gradientOverlayOrientation), modifier) {
        ImageFromUrl(
            imageUrl = imageUrl,
            modifier = Modifier.fillMaxSize(),
            placeholderRes = placeholderRes,
            contentDescription = contentDescription,
            contentScale = contentScale,
            enableCrossfade = enableCrossfade,
        )
    }
}

/**
 * Image of the given [Painter] with a gradient overlay in the orientation given in [gradientOverlayOrientation].
 */
@Composable
fun ImageWithGradientOverlay(painter: Painter,
                             modifier: Modifier = Modifier,
                             contentDescription: String? = null,
                             contentScale: ContentScale = ContentScale.Crop,
                             gradientOverlayOrientation: GradientOverlayOrientation =
                                 GradientOverlayOrientation.BOTTOM_TO_TOP,
                             gradientColors: List<Color>) {

    GradientOverlayContainer(GradientBrush.from(gradientColors, gradientOverlayOrientation), modifier) {
        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = contentScale,
            painter = painter,
            contentDescription = contentDescription,
        )
    }
}

/**
 * Gradient overlay using the given [GradientBrush] that is painted on top of the given [content].
 */
@Composable
fun GradientOverlayContainer(gradientBrush: GradientBrush,
                             modifier: Modifier = Modifier,
                             content: @Composable BoxScope.() -> Unit) {
    Box(modifier) {
        content()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(ContentAlpha.medium)
                .background(brush = gradientBrush.brush)
        )
    }
}

/**
 * Orientation for the gradient used in [ImageWithGradientOverlay].
 */
enum class GradientOverlayOrientation {
    LEFT_TO_RIGHT,
    RIGHT_TO_LEFT,
    TOP_TO_BOTTOM,
    BOTTOM_TO_TOP,
    RIGHT_TOP_TO_LEFT_BOTTOM,
    LEFT_BOTTOM_TO_RIGHT_TOP;
}

/**
 * Gradient bush in the orientation given in [gradientOverlayOrientation] using the given [gradientColors].
 */
class GradientBrush private constructor(val brush: Brush) {
    companion object {
        @Suppress("UndocumentedPublicFunction")
        fun from(gradientColors: List<Color>,
                 orientation: GradientOverlayOrientation): GradientBrush =
            GradientBrush(when (orientation) {
                GradientOverlayOrientation.LEFT_TO_RIGHT -> Brush.horizontalGradient(gradientColors.reversed())
                GradientOverlayOrientation.RIGHT_TO_LEFT -> Brush.horizontalGradient(gradientColors)
                GradientOverlayOrientation.TOP_TO_BOTTOM -> Brush.verticalGradient(gradientColors.reversed())
                GradientOverlayOrientation.BOTTOM_TO_TOP -> Brush.verticalGradient(gradientColors)
                GradientOverlayOrientation.RIGHT_TOP_TO_LEFT_BOTTOM ->
                    Brush.linearGradient(
                        colors = gradientColors,
                        start =  Offset(Float.POSITIVE_INFINITY, 0f),
                        end = Offset(0f, Float.POSITIVE_INFINITY)
                    )
                GradientOverlayOrientation.LEFT_BOTTOM_TO_RIGHT_TOP ->
                    Brush.linearGradient(
                        colors = gradientColors,
                        start = Offset(0f, Float.POSITIVE_INFINITY),
                        end = Offset(Float.POSITIVE_INFINITY, 0f)
                    )
            })
    }
}