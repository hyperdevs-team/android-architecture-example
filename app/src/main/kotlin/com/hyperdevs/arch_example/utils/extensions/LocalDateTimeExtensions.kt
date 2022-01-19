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

@file:Suppress("StringLiteralDuplication")
package com.hyperdevs.arch_example.utils.extensions

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

/**
 * Format [LocalDateTime] showing the day of the week and the time.
 * Ex: Monday 28, 13:45
 */
fun LocalDateTime.toDayOfTheWeekAndTimeString(locale: Locale = Locale.getDefault()): String {
    val formatter = DateTimeFormatter.ofPattern("EEEE dd, hh:mm", locale)
    return formatter.format(this)
}

/**
 * Format [LocalDateTime] showing the day of the week and the month.
 * Ex: Thursday 02 September
 */
fun LocalDateTime.toDayOfTheWeekAndMonthString(locale: Locale = Locale.getDefault()): String {
    val pattern = if (locale.language == "es") "EEEE d 'de' MMMM" else "EEEE dd MMMM"
    val formatter = DateTimeFormatter.ofPattern(pattern, locale)
    return formatter.format(this)
}

/**
 * Format [LocalDate] showing the day of the week and the month.
 * Ex: 02 September
 */
fun LocalDateTime.toDayAndMonthString(locale: Locale = Locale.getDefault()): String {
    val pattern = if (locale.language == "es") "d 'de' MMMM" else "dd MMMM"
    val formatter = DateTimeFormatter.ofPattern(pattern, locale)
    return formatter.format(this)
}

/**
 * Format [LocalDateTime] showing the day of the week and the month.
 * Ex: Thursday, July 29, 2021
 */
fun LocalDateTime.toDayOfTheWeekMonthAndYearString(locale: Locale = Locale.getDefault()): String {
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(locale)
    return formatter.format(this)
}

/**
 * Format [LocalDateTime] showing the month and the year.
 * Ex: July 2021
 */
fun LocalDateTime.toMonthAndYearString(locale: Locale = Locale.getDefault()): String {
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy").withLocale(locale)
    return formatter.format(this)
}

/**
 * Format [LocalDateTime] showing the day, month and the year.
 * Ex: 3 July 2021
 */
fun LocalDateTime.toDayMonthAndYearString(locale: Locale = Locale.getDefault()): String {
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy").withLocale(locale)
    return formatter.format(this)
}

/**
 * Check if a [LocalDateTime] is today.
 */
fun LocalDateTime.isToday(): Boolean = this.toLocalDate().isEqual(LocalDate.now())

/**
 * Return the number of days from now until this [LocalDateTime].
 */
fun LocalDateTime.daysFromToday() = Duration.between(LocalDateTime.now(), this).toDays() + 1