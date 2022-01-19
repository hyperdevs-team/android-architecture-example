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

import mini.Resource
import mini.Task
import mini.map
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

/**
 * Convert this [Resource] in to a [Task].
 */
fun Resource<*>.toTask(): Task = this.map { null }

/**
 * Convert this [Resource] of any type (including [Task] and [TimestampedTask] in to a new [Resource] with the given [value].
 */
fun <T> Resource<*>.toResource(value: T?): Resource<T> = when {
    isSuccess -> Resource.success(value)
    isFailure -> Resource.failure(exceptionOrNull())
    isLoading -> Resource.loading(value)
    else -> Resource.empty()
}

typealias TimestampedTask = Resource<LocalDateTime>

/**
 * Returns true if since the last time that this [TimestampedTask] was successful has already passed at least the amount
 * of time given in [amountForOutdated] in the [temporalUnit].
 *
 * The [temporalUnit] must have a duration that divides into the length of a standard day without remainder.
 * This includes all supplied time units on ChronoUnit until DAYS as the biggest one. Other units throw an exception.
 */
fun TimestampedTask.isOutdated(amountForOutdated: Long = 1, temporalUnit: TemporalUnit = ChronoUnit.DAYS): Boolean =
    getOrNull()?.let {
        LocalDateTime.now()
            // This truncates the local date time of now to the shortest temporal unit of [temporalAmountForOutdated]
            .truncatedTo(temporalUnit)
            .isAfter(it.plus(amountForOutdated, temporalUnit))
    } ?: false

/**
 * Returns a [TimestampedTask] in success state with the current [LocalDateTime].
 */
fun Resource.Companion.timestampedSuccess(): TimestampedTask = success(LocalDateTime.now())

/**
 * Returns true if this [TimestampedTask] is not success or if it is success but it is already outdated since the last
 * time it was a success.
 *
 * The [temporalUnit] must have a duration that divides into the length of a standard day without remainder.
 * This includes all supplied time units on ChronoUnit until DAYS as the biggest one. Other units throw an exception.
 */
fun TimestampedTask.isNotSuccessOrOutdated(amountForOutdated: Long = 1, temporalUnit: TemporalUnit = ChronoUnit.DAYS) =
    !this.isSuccess || isOutdated(amountForOutdated, temporalUnit)

/**
 * Convert this [Resource] in to a [TimestampedTask], a task which value when it is success is the [LocalDateTime] in
 * which it was set to success.
 */
fun Resource<*>.toTimestampedTask(): TimestampedTask = this.map { LocalDateTime.now() }