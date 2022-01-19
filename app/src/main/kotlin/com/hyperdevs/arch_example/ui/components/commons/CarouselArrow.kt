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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.ContentAlpha
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch

/**
 * Arrows used to go to the next or previous page in a element that contains a [PagerState].
 */
@Composable
fun CarouselArrow(modifier: Modifier = Modifier,
                  isStartArrow: Boolean,
                  @DrawableRes arrowIconRes: Int,
                  pagerState: PagerState,
                  infiniteLoop: Boolean,
                  isPagerReversed: Boolean = false) {
    val coroutineScope = rememberCoroutineScope()
    val currentPage = pagerState.currentPage
    val isEnabled = when {
        infiniteLoop -> true
        isStartArrow && !isPagerReversed || !isStartArrow && isPagerReversed -> currentPage != 0
        else -> currentPage != pagerState.pageCount - 1
    }
    IconButton(
        modifier = modifier,
        enabled = isEnabled,
        onClick = {
            coroutineScope.launch {
                @Suppress("UnnecessaryParentheses")
                val nextPage =
                    if ((isStartArrow && !isPagerReversed) || (!isStartArrow && isPagerReversed)) currentPage - 1
                    else currentPage + 1
                pagerState.animateScrollToPage(nextPage)
            }
        }
    ) {
        Image(
            modifier = Modifier.fillMaxHeight()
                .let {
                    if (!isEnabled) it.alpha(ContentAlpha.disabled)
                    else it
                },
            painter = painterResource(arrowIconRes),
            contentDescription = null
        )
    }
}