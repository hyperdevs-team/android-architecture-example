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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.hyperdevs.arch_example.validations.ValidationFieldType
import com.hyperdevs.arch_example.validations.ValidationViewData
import com.hyperdevs.arch_example.validations.getErrorText

/**
 * Component for a form field.
 */
@Suppress("LongParameterList")
@Composable
fun FormField(value: String,
              onValueChanged: (String) -> Unit,
              modifier: Modifier = Modifier,
              @StringRes labelTextRes: Int? = null,
              @StringRes placeholderTextRes: Int? = null,
              validation: ValidationViewData? = null,
              validationFieldType: ValidationFieldType = ValidationFieldType.DEFAULT,
              keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
              keyboardActions: KeyboardActions = KeyboardActions.Default,
              leadingIcon: @Composable (() -> Unit)? = null,
              trailingIcon: @Composable (() -> Unit)? = null,
              textFieldColors: TextFieldColors = TextFieldDefaults.textFieldColors(),
              textStyle: TextStyle = LocalTextStyle.current,
              labelTextStyle: TextStyle = textStyle,
              placeholderTextStyle: TextStyle = textStyle,
              maxLines: Int = Int.MAX_VALUE,
              isSingleLine: Boolean = false,
              errorTextPadding: PaddingValues = PaddingValues(top = 4.dp),
              isEnabled: Boolean = true,
              isReadOnly: Boolean = false,
              shape: Shape =
                  MaterialTheme.shapes.small.copy(bottomEnd = ZeroCornerSize, bottomStart = ZeroCornerSize)) {
    val localContext = LocalContext.current
    Column {
        TextField(
            modifier = modifier,
            value = value,
            onValueChange = {
                onValueChanged(it)
            },
            label = labelTextRes?.let {
                // If we apply a style to the label, when something is written and the label is moved up, it doesn't
                // change its size, so don't apply a style or font size to it
                { Text(text = stringResource(it), style = labelTextStyle) }
            },
            placeholder = placeholderTextRes?.let {
                { Text(text = stringResource(it), style = placeholderTextStyle) }
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            isError = validation?.isValid == false,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            colors = textFieldColors,
            textStyle = textStyle,
            maxLines = maxLines,
            enabled = isEnabled,
            readOnly = isReadOnly,
            singleLine = isSingleLine,
            shape = shape
        )

        // TODO: Workaround because there isn't a param for the text error right now in compose
        validation?.status?.getErrorText(localContext, validationFieldType)?.let {
            FormFieldErrorText(it, Modifier.padding(errorTextPadding))
        }
    }
}

/**
 * Colors for filled form fields without focused indicators.
 */
val SimpleFilledFormFieldColors: TextFieldColors
    @Composable get() = TextFieldDefaults.textFieldColors(
        backgroundColor = MaterialTheme.colors.surface,
        focusedLabelColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium),
        placeholderColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium),
        leadingIconColor = MaterialTheme.colors.secondary,
        trailingIconColor = MaterialTheme.colors.secondary,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        cursorColor = MaterialTheme.colors.onSurface,
    )