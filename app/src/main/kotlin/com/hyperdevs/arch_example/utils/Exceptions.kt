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

package com.hyperdevs.arch_example.utils

/**
 * Generic exceptions to use when returning [Resource] errors so we can type the different errors.
 */

/** App's domain [Exception]. **/
open class AppException(cause: String? = null) : Exception(cause)

/** App's domain [IllegalStateException]. */
open class IllegalStateAppException(cause: String? = null): IllegalStateException(cause)

/** App's domain [IllegalArgumentException]. */
open class IllegalArgumentAppException(cause: String? = null): IllegalArgumentException(cause)

/**
 * Exception thrown when a specific [Character] can not be found in the retrieved challenges.
 */
class CharacterNotFoundException(characterId: Int): AppException("Not found character with id [$characterId]")

/**
 * Exception thrown when the [Response] from a request to the Disney API is not successful.
 */
class DisneyApiRequestException(cause: String? = null): AppException(cause)

/* Uncomment if Firebase Auth is used
/** Exception thrown when the Firebase user is not valid. **/
object UserAccountNotFoundException : AppException()

/** Exception thrown when the user profile data is not valid. **/
object UserProfileDataNotFoundException : AppException()
 */