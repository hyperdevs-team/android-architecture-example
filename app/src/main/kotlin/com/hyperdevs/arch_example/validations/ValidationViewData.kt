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

package com.hyperdevs.arch_example.validations

import android.content.Context
import com.hyperdevs.arch_example.R
import kotlinx.coroutines.flow.StateFlow

/**
 * Generic ViewData used to validate input fields.
 */
@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
data class ValidationViewData(val status: ValidationStatus) {

    companion object {
        fun fromSimpleStringField(string: String?): ValidationViewData =
            ValidationViewData(if (string.isNullOrBlank()) ValidationStatus.Required else ValidationStatus.Valid)

        fun fromSimpleField(field: Any?): ValidationViewData =
            ValidationViewData(if (field == null) ValidationStatus.Required else ValidationStatus.Valid)
    }

    val isValid: Boolean = status.isValid()
}

@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
data class FormValidationViewData(val isValid: Boolean) {
    companion object {
        fun from(vararg validations: ValidationViewData) =
            FormValidationViewData(validations.all { it.isValid })
    }
}

/**
 * Possible validation states for form fields.
 */
sealed class ValidationStatus {
    /**
     * The input field is valid.
     */
    object Valid : ValidationStatus()

    /**
     * The input field is blank or empty, thus required.
     */
    object Required : ValidationStatus()

    /**
     * The input field has an invalid length.
     *
     * Query [minimumLength] to check the minimum length of the field and [matchExactly]
     * to check if the length must match exactly the value provided by [minimumLength] or it
     * can be bigger than said value.
     */
    data class InvalidLength(val minimumLength: Int, val matchExactly: Boolean = false) : ValidationStatus()

    /**
     * The input field has a wrong format.
     */
    data class WrongFormat(val acceptedFormat: AcceptedFormat) : ValidationStatus() {
        /**
         * Enumerates kinds of accepted input formats.
         */
        enum class AcceptedFormat {
            NUMERIC, // Only numbers
            ALPHABETIC, // Only letters
            ALPHANUMERIC, // Letters and numbers
            TEXT, // Any text
            EMAIL, // E-Mail
        }
    }

    /**
     * The password and confirm password values don't match.
     */
    object PasswordsDoNotMatch : ValidationStatus()

    /**
     * Returns true if the status is valid, false otherwise.
     */
    fun isValid(): Boolean = this is Valid
}

/**
 * Extension function that returns a different string depending on the kind of error and the [ValidationFieldType]
 * or null if the [ValidationStatus] is not an error.
 */
@Suppress("ComplexMethod")
fun ValidationStatus.getErrorText(context: Context,
                                  validationFieldType: ValidationFieldType = ValidationFieldType.DEFAULT): String? =
    when (this) {
        is ValidationStatus.Required -> {
            context.getString(
                when (validationFieldType) {
                    ValidationFieldType.NAME -> R.string.form_name_field_error
                    ValidationFieldType.LAST_NAME -> R.string.form_last_name_field_error
                    ValidationFieldType.EMAIL -> R.string.form_email_field_empty_error
                    ValidationFieldType.PASSWORD -> R.string.form_password_field_empty_error
                    ValidationFieldType.PHONE -> R.string.form_phone_field_empty_error
                    ValidationFieldType.DEFAULT -> R.string.form_generic_field_required_error
                }
            )
        }
        is ValidationStatus.InvalidLength -> {
            context.getString(
                when {
                    validationFieldType == ValidationFieldType.PASSWORD -> R.string.form_password_field_length_error
                    matchExactly -> R.string.form_invalid_length_exact_error
                    else -> R.string.form_invalid_length_error
                }
            )
        }
        is ValidationStatus.WrongFormat -> {
            if (validationFieldType == ValidationFieldType.EMAIL) {
                context.getString(R.string.form_email_field_wrong_format_error)
            } else {
                null
            }
        }
        is ValidationStatus.PasswordsDoNotMatch -> {
            context.getString(R.string.form_confirm_password_do_not_match)
        }
        else -> null
    }

/**
 * Different validation field types.
 */
enum class ValidationFieldType {
    NAME,
    LAST_NAME,
    EMAIL,
    PASSWORD,
    PHONE,
    DEFAULT
}

/**
 * Checks if the value contained in a validation flow is valid.
 */
fun StateFlow<ValidationViewData>.isValid(): Boolean = value.isValid

/**
 * Checks if the value contained in a validation flow is valid.
 */
fun StateFlow<FormValidationViewData>.isValidForm(): Boolean = value.isValid