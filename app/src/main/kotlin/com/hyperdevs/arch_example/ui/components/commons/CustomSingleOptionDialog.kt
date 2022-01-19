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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hyperdevs.arch_example.ui.theme.Dimens

/**
 * Dialog with a single option list of items. [onSingleChoiceSelected] will be invoked each time a user click on an
 * option.
 */
@Suppress("LongParameterList")
@Composable
fun <T> CustomSingleOptionDialog(modifier: Modifier,
                                 @StringRes titleRes: Int,
                                 singleChoiceItems: Map<Int, T>,
                                 selectedChoiceValue: T? = null,
                                 @StringRes positiveButtonTextRes: Int? = null,
                                 @StringRes negativeButtonTextRes: Int? = null,
                                 onSingleChoiceSelected: (selectedChoice: T) -> Unit = {},
                                 onPositiveButtonClick: (selectedChoice: T) -> Unit = {},
                                 onNegativeButtonClick: () -> Unit = {},
                                 onDismiss: () -> Unit = {}) {
    CustomSingleOptionDialog(
        modifier = modifier,
        title = stringResource(titleRes),
        singleChoiceItems = singleChoiceItems.mapKeys { (label, _) -> stringResource(label) },
        selectedChoiceValue = selectedChoiceValue,
        positiveButtonText = positiveButtonTextRes?.let { stringResource(it) },
        negativeButtonText = negativeButtonTextRes?.let { stringResource(negativeButtonTextRes) },
        onSingleChoiceSelected = onSingleChoiceSelected,
        onPositiveButtonClick = onPositiveButtonClick,
        onNegativeButtonClick = onNegativeButtonClick,
        onDismiss = onDismiss
    )
}

/**
 * Dialog with a single option list of items. [onSingleChoiceSelected] will be invoked each time a user click on an
 * option.
 */
@Suppress("LongParameterList", "LongMethod")
@Composable
fun <T> CustomSingleOptionDialog(modifier: Modifier,
                                 title: String,
                                 singleChoiceItems: Map<String, T>,
                                 selectedChoiceValue: T? = null,
                                 positiveButtonText: String? = null,
                                 negativeButtonText: String? = null,
                                 onSingleChoiceSelected: (selectedChoice: T) -> Unit = {},
                                 onPositiveButtonClick: (selectedChoice: T) -> Unit = {},
                                 onNegativeButtonClick: () -> Unit = {},
                                 onDismiss: () -> Unit = {}) {

    val showDialog = remember { mutableStateOf(true) }

    val (selectedOption, onOptionSelected) = remember { mutableStateOf(selectedChoiceValue) }

    if (showDialog.value) {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                showDialog.value = false
                onDismiss()
            },
            title = {
                Text(
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    text = title
                )
            },
            text = {
                Column(Modifier.selectableGroup(), Arrangement.spacedBy(16.dp)) {
                    singleChoiceItems.forEach { (label, value) ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = value == selectedOption,
                                    onClick = {
                                        onOptionSelected(value)
                                        onSingleChoiceSelected(value)
                                    },
                                    role = Role.RadioButton
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = value == selectedOption,
                                onClick = null, // null recommended for accessibility with screenreaders
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
                                    unselectedColor = MaterialTheme.colors.secondary,
                                ),
                                modifier = Modifier.padding(
                                    end = Dimens.generalDimens.button_icon_padding_horizontal
                                ),
                            )
                            Text(
                                text = label,
                                style = MaterialTheme.typography.subtitle2,
                                color = MaterialTheme.colors.onSurface
                            )
                        }
                    }
                }
            },
            confirmButton = {
                positiveButtonText?.let {
                    TextButton(
                        onClick = {
                            selectedOption?.let {
                                showDialog.value = false
                                onPositiveButtonClick(it)
                                onDismiss()
                            }
                        }
                    ) {
                        Text(
                            color = MaterialTheme.colors.onBackground,
                            text = positiveButtonText.uppercase()
                        )
                    }
                }
            },
            dismissButton = {
                if (negativeButtonText != null) {
                    TextButton(
                        onClick = {
                            showDialog.value = false
                            onNegativeButtonClick()
                        }
                    ) {
                        Text(
                            color = MaterialTheme.colors.onBackground,
                            text = negativeButtonText.uppercase()
                        )
                    }
                }
            }
        )
    }
}