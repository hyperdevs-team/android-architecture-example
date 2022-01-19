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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.hyperdevs.arch_example.ui.theme.Dimens

@Suppress("UndocumentedPublicFunction")
@Composable
fun MaterialChip(label: String,
                 color: Color,
                 modifier: Modifier = Modifier,
                 onClick: (() -> Unit)? = null) {
    Surface(
        shape = CircleShape,
        border = BorderStroke(
            width = Dimens.generalDimens.material_chip_border_width,
            color = color
        ),
        modifier = modifier
    ) {
        Text(
            text = label,
            modifier = Modifier
                .let { onClick?.let { clickFn -> it.clickable(onClick = clickFn) } ?: it }
                .padding(
                    vertical = Dimens.generalDimens.material_chip_vertical_padding,
                    horizontal = Dimens.generalDimens.material_chip_horizontal_padding
                ),
            style = MaterialTheme.typography.body2.copy(
                color = color,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = Dimens.themeDimens.font_letter_spacing_3
            )
        )
    }
}