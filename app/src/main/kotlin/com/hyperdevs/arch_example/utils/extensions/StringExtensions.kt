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

/**
 * Returns a new string obtained by replacing the last occurrence of the [oldValue] substring in this string
 * with the specified [newValue] string.
 */
fun String.replaceLast(oldValue: String, newValue: String, ignoreCase: Boolean = false): String {
    val index = lastIndexOf(oldValue, ignoreCase = ignoreCase)
    return if (index < 0) this else this.replaceRange(index, index + oldValue.length, newValue)
}

/**
 * Get the [numberOfLetters] letters from this [String], by obtaining the first letter of each word.
 * Ex: "Beyoncé Knowles Carter" -> "BK"
 * Ex: "beyonce.knowles" -> B
 */
fun String.initialLetters(numberOfLetters: Int = 2) =
    this.takeIf { it.isNotBlank() }
        ?.trim()
        ?.split(" ")
        ?.map { it.firstOrNull() }
        ?.joinToString(separator = "")
        ?.take(numberOfLetters)
        ?.uppercase()

/**
 * Split this [String] in to a list of its words.
 */
fun String.splitWords(): List<String> = if (isBlank()) emptyList() else trim().split("\\s+".toRegex())

/**
 * Get the number of words from this [String].
 */
fun String.wordsCount(): Int = splitWords().size

/**
 * Return all the indexes of the given [subString] occurrences in this String.
 */
fun String.indexesOf(subString: String, ignoreCase: Boolean = true): List<Int> =
    if (isBlank()) {
        emptyList()
    } else {
        val regex = if (ignoreCase) Regex(subString, RegexOption.IGNORE_CASE) else Regex(subString)
        regex.findAll(this).map { it.range.first }.toList()
    }
