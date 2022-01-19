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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hyperdevs.arch_example.ui.theme.Dimens

/**
 * Component for an error in a form field.
 */
@Composable
fun FormFieldErrorText(@StringRes textRes: Int, modifier: Modifier) {
    FormFieldErrorText(stringResource(textRes), modifier)
}

/**
 * Component for an error in a form field.
 */
@Composable
fun FormFieldErrorText(text: String, modifier: Modifier) {
    Text(
        text = text,
        color = MaterialTheme.colors.error,
        style = MaterialTheme.typography.caption,
        fontWeight = FontWeight.Medium,
        letterSpacing = Dimens.themeDimens.font_letter_spacing_3,
        modifier = modifier
    )
}