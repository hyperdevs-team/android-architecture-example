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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.google.accompanist.insets.ui.TopAppBar

/**
 * Top bar for the Entrena Virtual app.
 */
@Suppress("LongParameterList")
@Composable
fun AppTopBar(modifier: Modifier = Modifier,
              @StringRes titleRes: Int? = null,
              navigationIcon: Painter? = null,
              onNavigationIconClick: () -> Unit = {},
              navigationIconTint: Color? = null,
              navigationIconPaddingValues: PaddingValues? = null,
              backgroundColor: Color = MaterialTheme.colors.primarySurface,
              contentColor: Color = contentColorFor(backgroundColor),
              titleMaxLines: Int? = null,
              elevation: Dp = AppBarDefaults.TopAppBarElevation,
              actions: @Composable RowScope.() -> Unit = {}) {

    AppTopBar(modifier, titleRes?.let { stringResource(it) }, navigationIcon, onNavigationIconClick, navigationIconTint,
        navigationIconPaddingValues, backgroundColor, contentColor, titleMaxLines, elevation, actions)
}
/**
 * Top bar for the Entrena Virtual app.
 */
@Suppress("LongParameterList")
@Composable
fun AppTopBar(modifier: Modifier = Modifier,
              title: String? = null,
              navigationIcon: Painter? = null,
              onNavigationIconClick: () -> Unit = {},
              navigationIconTint: Color? = null,
              navigationIconPaddingValues: PaddingValues? = null,
              backgroundColor: Color = MaterialTheme.colors.primarySurface,
              contentColor: Color = contentColorFor(backgroundColor),
              titleMaxLines: Int? = null,
              elevation: Dp = AppBarDefaults.TopAppBarElevation,
              actions: @Composable RowScope.() -> Unit = {}) {
    TopAppBar(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        navigationIcon = navigationIcon?.let {
            {
                IconButton(
                    onClick = onNavigationIconClick,
                    modifier =
                    navigationIconPaddingValues?.let { Modifier.padding(navigationIconPaddingValues) } ?: Modifier
                ) {
                    Icon(
                        painter = it,
                        contentDescription = null,
                        tint = navigationIconTint ?: LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                    )
                }
            }
        },
        title = {
            title?.let {
                Text(
                    text = it,
                    maxLines = titleMaxLines ?: Int.MAX_VALUE,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        elevation = elevation,
        modifier = modifier,
        actions = actions
    )
}