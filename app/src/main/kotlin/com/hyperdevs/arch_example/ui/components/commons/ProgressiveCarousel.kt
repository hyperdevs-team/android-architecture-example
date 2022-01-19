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

import androidx.annotation.IntRange
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.hyperdevs.arch_example.utils.extensions.floorMod
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val SELECTED_PAGE_INITIAL_PROGRESS = 0.1f
private const val NOT_SELECTED_PAGE_PROGRESS = 0f
private const val PROGRESS_BAR_MAX_PROGRESS = 1f
private const val PAGER_INFINITE_LOOP_PAGE_COUNT = Int.MAX_VALUE

/**
 * Carousel of the given composables in [pages]. It will pass to the next page after the time set in
 * [delayBetweenPagesInMillis]. The pass of that time will be reflected in the indicator of that page.
 * Several modifiers for the different UI parts can be configured.
 * Use [offscreenLoadedPagesCount] to load the given previous and following pages along with the current page.
 */
@Suppress("LongParameterList")
@Composable
fun ProgressiveCarousel(carouselModifier: Modifier,
                        indicatorsRowAlignment: Alignment = BottomCenter,
                        indicatorsRowModifier: Modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth(),
                        indicatorModifier: Modifier = Modifier
                            .widthIn(4.dp, 40.dp)
                            .padding(4.dp),
                        indicatorColor: Color = MaterialTheme.colors.primary,
                        elevation: Dp = 10.dp,
                        @IntRange(from = 0) delayBetweenPagesInMillis: Int,
                        shape: Shape = MaterialTheme.shapes.medium,
                        pages: List<@Composable (isPageVisible: Boolean) -> Unit>) {

    val actualPagesCount = pages.count()
    val useInfiniteLoop = actualPagesCount > 1
    // We start the pager in the middle of the raw number of pages in case of infinite loop
    val initialPageIndex = if (useInfiniteLoop) PAGER_INFINITE_LOOP_PAGE_COUNT / 2 else actualPagesCount
    val pagerState = rememberPagerState(initialPage = initialPageIndex)
    val scope = rememberCoroutineScope()

    Card(modifier = carouselModifier.fillMaxSize(), elevation = elevation, shape = shape) {
        Box(modifier = Modifier.fillMaxSize()) {
            HorizontalPager(
                state = pagerState,
                count = if (useInfiniteLoop) PAGER_INFINITE_LOOP_PAGE_COUNT else actualPagesCount,
                modifier = Modifier.fillMaxSize()
            ) { index ->
                // Calculate the page actual index from the given index
                val actualPageIndex = (index - initialPageIndex).floorMod(actualPagesCount)
                val targetPageIndex = (pagerState.targetPage - initialPageIndex).floorMod(actualPagesCount)

                @Suppress("UnnecessaryParentheses")
                val isPageSelectedOrBeingSelected =
                    (targetPageIndex == actualPageIndex && pagerState.currentPage != pagerState.targetPage)
                        || pagerState.currentPage == index
                pages[actualPageIndex](isPageVisible = isPageSelectedOrBeingSelected)
            }
            if (actualPagesCount > 1) {
                ProgressiveCarouselIndicators(
                    indicatorsRowModifier = indicatorsRowModifier.align(indicatorsRowAlignment),
                    indicatorModifier = indicatorModifier,
                    indicatorColor = indicatorColor,
                    delayBetweenPagesInMillis = delayBetweenPagesInMillis,
                    pagerState = pagerState,
                    initialPageIndex = initialPageIndex,
                    actualPagesCount = actualPagesCount
                ) { pageIndexToScroll ->
                    scope.launch {
                        pagerState.animateScrollToPage(pageIndexToScroll)
                    }
                }
            }
        }
    }
}

/**
 * Indicator for each page of a [ProgressiveCarousel] which will change its UI with the pass of time as a [LinearProgressIndicator].
 */
@Composable
private fun ProgressiveCarouselIndicators(indicatorsRowModifier: Modifier,
                                          indicatorModifier: Modifier,
                                          indicatorColor: Color,
                                          @IntRange(from = 0) delayBetweenPagesInMillis: Int,
                                          pagerState: PagerState,
                                          initialPageIndex: Int,
                                          actualPagesCount: Int,
                                          scrollToPage: (Int) -> Unit) {
    var goToFollowingPageProgress by rememberSaveable { mutableStateOf(SELECTED_PAGE_INITIAL_PROGRESS) }

    val scope = rememberCoroutineScope()
    LaunchedEffect(pagerState) {
        var animationJob: Job? = null
        // We need to use [snapshotFlow] instead of [pagerState] directly because if not we get aware of the change of
        // page too late, and the animation in the indicator shows as full for a moment in the new page and it's very ugly
        // We also need to cancel the previous animation to fix this
        snapshotFlow { pagerState.currentPage to pagerState.isScrollInProgress }.collect { (page, isScrolling) ->
            animationJob?.cancel()
            animationJob = null

            if (!isScrolling) {
                animationJob = scope.launch {
                    animate(
                        initialValue = SELECTED_PAGE_INITIAL_PROGRESS,
                        targetValue = PROGRESS_BAR_MAX_PROGRESS,
                        animationSpec = tween(durationMillis = delayBetweenPagesInMillis, easing = LinearEasing)
                    ) { value, _ ->
                        goToFollowingPageProgress = value
                        if (value == PROGRESS_BAR_MAX_PROGRESS) {
                            // If we are in the last page, go to the first one again
                            val pageIndexToScroll = if (page == pagerState.pageCount - 1) initialPageIndex else page + 1
                            scrollToPage(pageIndexToScroll)
                        }
                    }
                }
            }
        }
    }

    // Calculate the page actual index from the given index
    val actualPageIndex = (pagerState.currentPage - initialPageIndex).floorMod(actualPagesCount)
    ProgressiveCarouselIndicatorsRow(
        actualPagesCount, actualPageIndex, goToFollowingPageProgress, indicatorColor,
        indicatorsRowModifier, indicatorModifier
    )
}

@Composable
private fun ProgressiveCarouselIndicatorsRow(pagesCount: Int,
                                             selectedPage: Int,
                                             selectedPageProgress: Float,
                                             color: Color,
                                             indicatorsRowModifier: Modifier,
                                             indicatorModifier: Modifier) {
    Row(
        modifier = indicatorsRowModifier,
        horizontalArrangement = Arrangement.Center
    ) {
        (0 until pagesCount).forEach { pageIndicatorIndex ->
            LinearProgressIndicator(
                progress = if (pageIndicatorIndex == selectedPage) selectedPageProgress else NOT_SELECTED_PAGE_PROGRESS,
                modifier = indicatorModifier.weight(1f),
                color = color
            )
        }
    }
}