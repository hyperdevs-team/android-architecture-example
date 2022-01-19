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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * A circular progress bar in the center of a full screen.
 */
@Composable
fun ScrimCircularProgressIndicator(modifier: Modifier,
                                   disableContentBelow: Boolean = true,
                                   color: Color = MaterialTheme.colors.primary,
                                   showScrimOnDisableContent: Boolean = false) {
    Box(
        modifier = modifier.let {
            // The clickable is to avoid the user touching anything below while loading
            // The enabled = false makes that when clicking the ripple is not shown
            if (disableContentBelow) {
                val disabledModifier = it.clickable(enabled = false) {}
                if (showScrimOnDisableContent) disabledModifier.background(Color.Black.copy(ContentAlpha.disabled))
                else disabledModifier
            } else {
                it
            }
        }
    ) {
        CircularProgressIndicator(color = color, modifier = Modifier.align(Alignment.Center))
    }
}