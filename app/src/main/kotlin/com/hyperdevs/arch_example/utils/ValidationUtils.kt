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

private const val REGEX1_EMAIL = "[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+"
private const val REGEX2_EMAIL =
    "(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*" +
    "@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+"
private const val REGEX3_EMAIL = "[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?"
val REGEX_EMAIL by lazy { (REGEX1_EMAIL + REGEX2_EMAIL + REGEX3_EMAIL).toRegex() }

/**
 * Omits email addresses containing double-quotes and square brackets,
 * which while valid according to <a href="http://www.faqs.org/rfcs/rfc2822.html">RFC 2822</a>,
 * are almost never used. We also allow uppercase letters.
 *
 * @see <a href="http://snipplr.com/view/19594/">Source</a>
 */
fun String.isValidEmail(): Boolean = REGEX_EMAIL.matches(this)