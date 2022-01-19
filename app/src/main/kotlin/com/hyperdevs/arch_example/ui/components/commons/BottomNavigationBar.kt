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
import androidx.compose.animation.Crossfade
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hyperdevs.arch_example.navigation.ScreenType
import com.hyperdevs.arch_example.utils.extensions.navigateToStartDestination

/**
 * Generic component for a bottom bar to navigate between [ScreenType]s.
 */
@Composable
fun BottomNavigationBar(navController: NavHostController,
                        items: List<BottomNavigationBarItem>,
                        modifier: Modifier = Modifier,
                        backgroundColor: Color = MaterialTheme.colors.primarySurface,
                        contentColor: Color = contentColorFor(backgroundColor),
                        elevation: Dp = BottomNavigationDefaults.Elevation,
                        showLabels: Boolean = true,
                        alwayShowLabels: Boolean = false) {
    BottomNavigation(
        modifier = modifier,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation = elevation
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach {
            val isSelected = currentDestination?.hierarchy?.any { destination ->
                // TODO: Change to any { destination -> destination.route == it.screenType.route } == true if the bottom
                //       bar has to disappear when going to the detail screen
                destination.route?.startsWith(it.screenType.route) == true
            } == true
            BottomNavigationItem(
                icon = {
                    if (it.selectedIconPainter != null) {
                        Crossfade(targetState = isSelected) { selected ->
                            Icon(
                                painter = if (selected) it.selectedIconPainter else it.iconPainter,
                                contentDescription = it.contentDescriptionRes?.let { stringResource(it) } ?: "",
                                tint = if (selected) MaterialTheme.colors.onSurface else MaterialTheme.colors.secondary
                            )
                        }
                    } else {
                        Icon(
                            painter = it.iconPainter,
                            contentDescription = it.contentDescriptionRes?.let { stringResource(it) } ?: "",
                            tint = if (isSelected) MaterialTheme.colors.onSurface else MaterialTheme.colors.secondary
                        )
                    }
                },
                label = if (showLabels) {
                    { Text(stringResource(it.labelRes), maxLines = 1, overflow = TextOverflow.Ellipsis) }
                } else {
                    null
                },
                selected = isSelected,
                onClick = {
                    if (currentDestination?.route == it.screenType.route) return@BottomNavigationItem
                    navController.navigateToStartDestination(it.screenType)
                    it.onSelectedFn?.let { fn -> fn(it.screenType) }
                },
                alwaysShowLabel = alwayShowLabels
            )
        }
    }
}

/**
 * Definition of an item for the [BottomNavigationBar].
 */
data class BottomNavigationBarItem(val screenType: ScreenType,
                                   @StringRes val labelRes: Int,
                                   val iconPainter: Painter,
                                   val selectedIconPainter: Painter? = null,
                                   @StringRes val contentDescriptionRes: Int? = null,
                                   val onSelectedFn: ((screenType: ScreenType) -> Unit)? = null)