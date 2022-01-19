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

package com.hyperdevs.arch_example.validations.traits

import com.hyperdevs.arch_example.utils.isValidEmail
import com.hyperdevs.arch_example.validations.ValidationStatus
import com.hyperdevs.arch_example.validations.ValidationViewData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Trait for ViewModels that need e-mail validations.
 */
interface EmailValidationTrait {
    val emailValidation: MutableStateFlow<ValidationViewData?>

    /**
     * Validates the e-mail.
     * CONTRACT: emits result via [emailValidation]
     */
    fun validateEmail(email: String)

    @Suppress("UndocumentedPublicFunction")
    fun getEmailValidation(): StateFlow<ValidationViewData?> = emailValidation
}

/**
 * Implementation of [EmailValidationTrait].
 */
class EmailValidationTraitImpl : EmailValidationTrait {
    override val emailValidation = MutableStateFlow<ValidationViewData?>(null)

    override fun validateEmail(email: String) {
        emailValidation.value = validateFrom(email)
    }

    private fun validateFrom(email: String): ValidationViewData = when {
        email.isBlank() -> ValidationViewData(ValidationStatus.Required)
        !email.isValidEmail() ->
            ValidationViewData(ValidationStatus.WrongFormat(ValidationStatus.WrongFormat.AcceptedFormat.EMAIL))
        else -> ValidationViewData(ValidationStatus.Valid)
    }
}