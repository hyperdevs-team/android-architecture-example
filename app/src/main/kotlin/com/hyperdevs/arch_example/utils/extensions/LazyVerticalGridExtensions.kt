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

package com.hyperdevs.arch_example.utils.extensions

import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems

/**
 * Use paginated items for the [LazyVerticalGrid].
 */
fun <T : Any> LazyGridScope.items(lazyPagingItems: LazyPagingItems<T>,
                                  itemContent: @Composable LazyGridItemScope.(value: T?) -> Unit) {
    items(lazyPagingItems.itemCount) { index ->
        itemContent(lazyPagingItems[index])
    }
}

/**
 * Use paginated items for the [LazyVerticalGrid] and in each item is aware of its index.
 */
fun <T : Any> LazyGridScope.itemsIndexed(lazyPagingItems: LazyPagingItems<T>,
                                         itemContent: @Composable LazyGridItemScope.(index: Int, value: T?) -> Unit) {
    items(lazyPagingItems.itemCount) { index ->
        itemContent(index, lazyPagingItems[index])
    }
}