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

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Component of a vertical grid that is not lazy, so it can be used inside other components that scrolls vertically,
 * without crashing.
 */
@Composable
fun <T> VerticalGrid(numberOfColumns: Int,
                     modifier: Modifier = Modifier,
                     items: List<T>,
                     contentPadding: PaddingValues = PaddingValues(0.dp),
                     spaceBetweenColumns: Dp = 0.dp,
                     spaceBetweenRows: Dp = 0.dp,
                     columnHorizontalAlignment: Alignment.Horizontal = Alignment.Start,
                     rowVerticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
                     contentView: @Composable (T) -> Unit) {
    val chunkedByNumberOfCells = items.chunked(numberOfColumns)
    Column(modifier = modifier.padding(contentPadding), horizontalAlignment = columnHorizontalAlignment) {
        chunkedByNumberOfCells.forEachIndexed { rowIndex, rowItems ->
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = rowVerticalAlignment) {
                for (columnIndex in 0 until numberOfColumns) {
                    if (columnIndex >= rowItems.count()) {
                        Spacer(modifier = Modifier.width(spaceBetweenColumns))
                        // This fills the empty spaces of the cells that doesn't contains space
                        Spacer(Modifier.weight(1f, fill = true))
                    } else {
                        if (columnIndex > 0) {
                            Spacer(modifier = Modifier.width(spaceBetweenColumns))
                        }
                        Box(
                            modifier = Modifier.weight(1f, fill = true),
                            propagateMinConstraints = true
                        ) {
                            contentView(rowItems[columnIndex])
                        }
                    }
                }
            }

            if (rowIndex != chunkedByNumberOfCells.lastIndex) {
                Spacer(modifier = Modifier.height(spaceBetweenRows))
            }
        }
    }
}

/**
 * Component of a vertical grid that is not lazy, so it can be used inside other components that scrolls vertically,
 * without crashing.
 * We have to pass the list of composables in the [contentViews] param.
 */
@Composable
fun VerticalGrid(numberOfColumns: Int,
                 modifier: Modifier = Modifier,
                 contentPadding: PaddingValues = PaddingValues(0.dp),
                 spaceBetweenColumns: Dp = 0.dp,
                 spaceBetweenRows: Dp = 0.dp,
                 columnHorizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
                 rowVerticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
                 contentViews: List<@Composable () -> Unit>) {
    val chunkedByNumberOfCells = contentViews.chunked(numberOfColumns)
    Column(modifier = modifier.padding(contentPadding), horizontalAlignment = columnHorizontalAlignment) {
        chunkedByNumberOfCells.forEachIndexed { rowIndex, rowItems ->
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = rowVerticalAlignment) {
                for (columnIndex in 0 until numberOfColumns) {
                    if (columnIndex >= rowItems.count()) {
                        Spacer(modifier = Modifier.width(spaceBetweenColumns))
                        // This fills the empty spaces of the cells that doesn't contains space
                        Spacer(Modifier.weight(1f, fill = true))
                    } else {
                        if (columnIndex > 0) {
                            Spacer(modifier = Modifier.width(spaceBetweenColumns))
                        }
                        Box(
                            modifier = Modifier.weight(1f, fill = true),
                            propagateMinConstraints = true
                        ) {
                            rowItems[columnIndex]()
                        }
                    }
                }
            }

            if (rowIndex != chunkedByNumberOfCells.lastIndex) {
                Spacer(modifier = Modifier.height(spaceBetweenRows))
            }
        }
    }
}