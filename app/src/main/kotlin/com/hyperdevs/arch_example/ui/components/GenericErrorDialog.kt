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

package com.hyperdevs.arch_example.ui.components

import androidx.compose.runtime.Composable
import com.hyperdevs.arch_example.R
import com.hyperdevs.arch_example.ui.components.commons.CustomDialog

/**
 * Generic error dialog.
 */
@Composable
fun GenericErrorDialog(onDismiss: () -> Unit) {
    CustomDialog(
        titleRes = R.string.generic_error_dialog_title,
        messageRes = R.string.generic_error_dialog_message,
        positiveButtonTextRes = android.R.string.ok,
        onDismiss = onDismiss
    )
}