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

package com.hyperdevs.arch_example.utils.extensions

import com.squareup.moshi.Moshi
import retrofit2.Response
import timber.log.Timber

/**
 * Error body or error message from a Disney API response.
 * The name of these fields may vary depending on the different APIs.
 */
data class ErrorBody(val error: String? = null, val message: String? = null)

/**
 * Return null if the response is successful, and parse the error body or the message of the response if not.
 */
@Suppress("NestedBlockDepth")
fun Response<*>.parseDisneyBodyError(moshi: Moshi): String? =
    if (isSuccessful) {
        null
    } else {
        try {
            val errorBodyAdapter = moshi.adapter(ErrorBody::class.java)
            val errorBodySource = errorBody()?.source()
            errorBodySource?.let { errorBuffer ->
                errorBodyAdapter.fromJson(errorBuffer)?.let { it.error ?: it.message }
            }
        } catch (throwable: Throwable) {
            Timber.v(throwable, "Error parsing the body error")
            null
        }
    }
