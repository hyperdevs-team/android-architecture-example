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
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.hyperdevs.arch_example.validations.ValidationFieldType
import com.hyperdevs.arch_example.validations.ValidationViewData
import com.hyperdevs.arch_example.validations.getErrorText

/**
 * Component for a password form field.
 */
@Suppress("LongParameterList")
@Composable
fun PasswordFormField(value: String,
                      validation: ValidationViewData?,
                      onValueChanged: (String) -> Unit,
                      modifier: Modifier = Modifier,
                      @StringRes labelTextRes: Int? = null,
                      @StringRes placeholderTextRes: Int? = null,
                      @DrawableRes passwordHiddenIconRes: Int,
                      @DrawableRes passwordShownIconRes: Int,
                      keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
                      keyboardActions: KeyboardActions = KeyboardActions.Default,
                      textFieldColors: TextFieldColors = TextFieldDefaults.textFieldColors(),
                      textStyle: TextStyle = LocalTextStyle.current,
                      labelTextStyle: TextStyle = textStyle,
                      placeholderTextStyle: TextStyle = textStyle,
                      errorTextPadding: PaddingValues = PaddingValues(top = 4.dp),
                      isError: Boolean = false,
                      shape: Shape =
                          MaterialTheme.shapes.small.copy(bottomEnd = ZeroCornerSize, bottomStart = ZeroCornerSize)) {

    val passwordHidden = rememberSaveable { mutableStateOf(true) }
    val localContext = LocalContext.current
    Column {
        val visualTransformation =
            if (passwordHidden.value) PasswordVisualTransformation() else VisualTransformation.None
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
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Password),
            keyboardActions = keyboardActions,
            trailingIcon = {
                IconButton(onClick = { passwordHidden.value = !passwordHidden.value }) {
                    val visibilityIcon = if (passwordHidden.value) passwordHiddenIconRes else passwordShownIconRes
                    Icon(painterResource(visibilityIcon), contentDescription = null)
                }
            },
            isError = validation?.isValid == false || isError,
            colors = textFieldColors,
            textStyle = textStyle,
            singleLine = true,
            shape = shape
        )
        // TODO: Workaround because there isn't a param for the text error right now in compose
        validation?.status?.getErrorText(localContext, ValidationFieldType.PASSWORD)?.let {
            FormFieldErrorText(it, Modifier.padding(errorTextPadding))
        }
    }
}