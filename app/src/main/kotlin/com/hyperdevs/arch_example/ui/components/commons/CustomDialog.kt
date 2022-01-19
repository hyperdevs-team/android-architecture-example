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
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight

/**
 * Customizable default dialog. Wrap it in a theme to show the buttons properly
 */
@Composable
fun CustomDialog(@StringRes titleRes: Int,
                 @StringRes messageRes: Int,
                 @StringRes positiveButtonTextRes: Int,
                 modifier: Modifier = Modifier,
                 @StringRes negativeButtonTextRes: Int? = null,
                 onPositiveButtonClick: () -> Unit = {},
                 onNegativeButtonClick: () -> Unit = {},
                 onDismiss: () -> Unit = {}) {
    CustomDialog(
        modifier = modifier,
        title = stringResource(titleRes),
        message = stringResource(messageRes),
        positiveButtonText = stringResource(positiveButtonTextRes),
        negativeButtonText = negativeButtonTextRes?.let { stringResource(negativeButtonTextRes) },
        onPositiveButtonClick = onPositiveButtonClick,
        onNegativeButtonClick = onNegativeButtonClick,
        onDismiss = onDismiss
    )
}

@Composable
@Suppress("UndocumentedPublicFunction")
fun CustomDialog(title: String,
                 message: String,
                 positiveButtonText: String,
                 modifier: Modifier = Modifier,
                 negativeButtonText: String? = null,
                 onPositiveButtonClick: () -> Unit = {},
                 onNegativeButtonClick: () -> Unit = {},
                 onDismiss: () -> Unit = {}) {

    val showDialog = remember { mutableStateOf(true) }

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
                Text(
                    style = MaterialTheme.typography.subtitle2,
                    text = message
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog.value = false
                        onPositiveButtonClick()
                        onDismiss()
                    }
                ) {
                    Text(
                        color = MaterialTheme.colors.onBackground,
                        text = positiveButtonText.uppercase()
                    )
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
            },
        )
    }
}