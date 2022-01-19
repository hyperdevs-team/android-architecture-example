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

import com.hyperdevs.arch_example.validations.ValidationStatus
import com.hyperdevs.arch_example.validations.ValidationViewData
import com.hyperdevs.arch_example.validations.traits.PasswordValidationTrait.Companion.MIN_PASSWORD_LENGTH
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Trait for ViewModels that need password validations.
 */
interface PasswordValidationTrait {
    companion object {
        const val MIN_PASSWORD_LENGTH = 6 // Firebase Auth requires this, so enforce it
    }

    val passwordValidation: MutableStateFlow<ValidationViewData?>

    /**
     * Validates the password.
     * CONTRACT: emits result via [passwordValidation]
     */
    fun validatePassword(password: String)

    @Suppress("UndocumentedPublicFunction")
    fun getPasswordValidation(): StateFlow<ValidationViewData?> = passwordValidation

}

/**
 * Implementation of [PasswordValidationTrait].
 */
class PasswordValidationTraitImpl : PasswordValidationTrait {
    override val passwordValidation = MutableStateFlow<ValidationViewData?>(null)

    override fun validatePassword(password: String) {
        passwordValidation.value = validateFrom(password)
    }

    private fun validateFrom(password: String): ValidationViewData =
        when {
            password.isBlank() -> ValidationViewData(ValidationStatus.Required)
            password.length < MIN_PASSWORD_LENGTH ->
                ValidationViewData(ValidationStatus.InvalidLength(MIN_PASSWORD_LENGTH))
            else -> ValidationViewData(ValidationStatus.Valid)
        }
}