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

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.hyperdevs.arch_example.ui.theme.Dimens

/**
 * Generic button with a possible start icon and/or end icon that can be filled or outlined.
 * If [contentHorizontalArrangement] is not [Arrangement.Center] the button will be shown as [fillMaxWidth] to fullfill
 * that [Arrangement]. Consider setting a fixed width if needed in that case using the [modifier] param.
 */
@Suppress("LongParameterList")
@Composable
fun ButtonWithIcons(text: String,
                    modifier: Modifier = Modifier,
                    startIconPainter: Painter? = null,
                    endIconPainter: Painter? = null,
                    iconWidth: Dp? = null,
                    iconHeight: Dp? = null,
                    isEnabled: Boolean = true,
                    isOutlined: Boolean = false,
                    colors: ButtonColors =
                        if (isOutlined) ButtonDefaults.outlinedButtonColors()
                        else ButtonDefaults.buttonColors(),
                    border: BorderStroke? = if (isOutlined) ButtonDefaults.outlinedBorder else null,
                    elevation: ButtonElevation? = if (isOutlined) null else ButtonDefaults.elevation(),
                    shape: CornerBasedShape = MaterialTheme.shapes.small,
                    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
                    contentHorizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
                    onClick: (() -> Unit)?) {
    if (onClick != null) {
        Button(
            onClick = onClick,
            modifier = modifier,
            colors = colors,
            border = border,
            contentPadding = contentPadding,
            shape = shape,
            elevation = elevation,
            enabled = isEnabled
        ) {
            ButtonWithIconsContent(
                Modifier, text, startIconPainter, endIconPainter, iconWidth, iconHeight, contentHorizontalArrangement)
        }
    } else {
        val contentColor by colors.contentColor(isEnabled)
        Surface(
            color = colors.backgroundColor(enabled = isEnabled).value,
            contentColor = contentColor,
            shape = shape,
            border = border,
        ) {
            CompositionLocalProvider(LocalContentAlpha provides contentColor.alpha) {
                ProvideTextStyle(
                    value = MaterialTheme.typography.button
                ) {
                    val contentModifier = Modifier
                        .defaultMinSize(
                            minWidth = ButtonDefaults.MinWidth,
                            minHeight = ButtonDefaults.MinHeight
                        )
                        .padding(ButtonDefaults.ContentPadding)
                    ButtonWithIconsContent(contentModifier, text, startIconPainter, endIconPainter, iconWidth,
                        iconHeight, contentHorizontalArrangement)
                }
            }
        }
    }
}

@Composable
private fun ButtonWithIconsContent(modifier: Modifier,
                                   text: String,
                                   startIconPainter: Painter?,
                                   endIconPainter: Painter?,
                                   iconWidth: Dp?,
                                   iconHeight: Dp?,
                                   contentHorizontalArrangement: Arrangement.Horizontal) {
    Row(
        modifier = if (contentHorizontalArrangement != Arrangement.Center) modifier.fillMaxWidth() else modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = contentHorizontalArrangement
    ) {
        val iconModifier = Modifier.let {
            when {
                iconWidth != null && iconHeight != null -> it.size(iconWidth, iconHeight)
                iconWidth != null -> it.width(iconWidth)
                iconHeight != null -> it.height(iconHeight)
                else -> it
            }
        }
        startIconPainter?.let {
            Icon(
                painter = startIconPainter,
                contentDescription = null,
                modifier = iconModifier
            )
            if (text.isNotBlank()) {
                Spacer(Modifier.padding(end = Dimens.generalDimens.button_icon_padding_horizontal))
            }
        }
        Text(text.uppercase())
        endIconPainter?.let {
            if (text.isNotBlank()) {
                Spacer(Modifier.padding(start = Dimens.generalDimens.button_icon_padding_horizontal))
            }
            Icon(
                painter = endIconPainter,
                contentDescription = null,
                modifier = iconModifier
            )
        }
    }
}

/**
 * Generic button with a possible start icon and/or end icon that can be filled or outlined.
 * If [contentHorizontalArrangement] is not [Arrangement.Center] the button will be shown as [fillMaxWidth] to fullfill
 * that [Arrangement]. Consider setting a fixed width if needed in that case using the [modifier] param.
 */
@Suppress("LongParameterList")
@Composable
fun ButtonWithIcons(@StringRes textRes: Int,
                    modifier: Modifier = Modifier,
                    startIconPainter: Painter? = null,
                    endIconPainter: Painter? = null,
                    iconWidth: Dp? = null,
                    iconHeight: Dp? = null,
                    isEnabled: Boolean = true,
                    isOutlined: Boolean = false,
                    colors: ButtonColors =
                        if (isOutlined) ButtonDefaults.outlinedButtonColors()
                        else ButtonDefaults.buttonColors(),
                    border: BorderStroke? = if (isOutlined) ButtonDefaults.outlinedBorder else null,
                    elevation: ButtonElevation? = if (isOutlined) null else ButtonDefaults.elevation(),
                    shape: CornerBasedShape = MaterialTheme.shapes.small,
                    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
                    contentHorizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
                    onClick: (() -> Unit)?) {

    ButtonWithIcons(stringResource(textRes), modifier, startIconPainter, endIconPainter, iconWidth, iconHeight,
        isEnabled, isOutlined, colors, border, elevation, shape, contentPadding, contentHorizontalArrangement, onClick)
}