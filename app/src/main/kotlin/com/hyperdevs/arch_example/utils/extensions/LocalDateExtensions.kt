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

import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.*

/**
 * Format [LocalDate] showing the day of the week and the day of the month.
 * Ex: Monday 28
 */
fun LocalDate.toDayOfTheWeekAndDay(locale: Locale = Locale.getDefault()): String {
    val formatter = DateTimeFormatter.ofPattern("EEEE dd", locale)
    return formatter.format(this)
}

/**
 * Format [LocalDate] showing the day of the month and the month.
 * Ex: 26 AGOSTO
 */
fun LocalDate.toDayAndMonth(locale: Locale = Locale.getDefault()): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM", locale)
    return formatter.format(this)
}

/**
 * Format [LocalDate] showing the month and the year.
 * Ex: July 2021
 */
fun LocalDate.toMonthAndYearString(locale: Locale = Locale.getDefault()): String {
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy").withLocale(locale)
    return formatter.format(this)
}

/**
 * Format [LocalDate] showing the day, month and the year.
 * Ex: 3 July 2021
 */
fun LocalDate.toDayMonthAndYearString(locale: Locale = Locale.getDefault()): String {
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy").withLocale(locale)
    return formatter.format(this)
}

/**
 * Format [LocalDate] showing the day of the week and the month.
 * Ex: 02 September
 */
fun LocalDate.toDayOfTheWeekNumberAndMonthString(locale: Locale = Locale.getDefault()): String {
    val pattern = if (locale.language == "es") "EEEE d 'de' MMMM" else "EEEE dd MMMM"
    val formatter = DateTimeFormatter.ofPattern(pattern, locale)
    return formatter.format(this)
}

/**
 * Format [LocalDate] showing the day of the week and the month.
 * Ex: 02 September
 */
fun LocalDate.toDayAndMonthString(locale: Locale = Locale.getDefault()): String {
    val pattern = if (locale.language == "es") "d 'de' MMMM" else "dd MMMM"
    val formatter = DateTimeFormatter.ofPattern(pattern, locale)
    return formatter.format(this)
}

/**
 * Calculate the number of days from now to the next sunday.
 */
fun LocalDate.daysToNextSunday(): Long {
    val nextSunday = this.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
    // Add one day because the second param is exclusive
    return ChronoUnit.DAYS.between(this, nextSunday) + 1
}

/**
 * Return the number of days from now until this [LocalDate].
 */
fun LocalDate.daysFromToday() = Duration.between(LocalDate.now(), this).toDays() + 1

/**
 * Check if a date is between two dates.
 */
fun LocalDate.isBetween(startDate: LocalDate, endDate: LocalDate) =
    this.isAfter(startDate) && this.isBefore(endDate) || this.isEqual(startDate) || this.isEqual(endDate)

/**
 * Check if two dates are in the same month.
 */
fun LocalDate.isInSameMonth(date: LocalDate) = this.month == date.month