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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.hyperdevs.arch_example.ui.theme.Dimens

@Suppress("UndocumentedPublicFunction")
@Composable
fun LoadingInfoScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.generalDimens.layout_padding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val placeHolderModifier = Modifier.placeholder(
            visible = true,
            highlight = PlaceholderHighlight.shimmer()
        )
        Text(
            text = "",
            style = MaterialTheme.typography.h4,
            modifier = placeHolderModifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(Dimens.generalDimens.loading_screen_spacer_size))
        Text(
            text = "",
            style = MaterialTheme.typography.body1,
            modifier = placeHolderModifier
                .fillMaxWidth(Dimens.generalDimens.loading_screen_placeholder_width_ratio)
        )
    }
}