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

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Custom adapter for using [LocalDateTime] with Moshi.
 */
class LocalDateTimeAdapter {
    @Suppress("UndocumentedPublicFunction")
    @ToJson
    fun toJson(value: LocalDateTime): String = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(value)

    @Suppress("UndocumentedPublicFunction")
    @FromJson
    fun fromJson(value: String): LocalDateTime = LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}

/**
 * Custom adapter for using [LocalTime] with Moshi.
 */
class LocalTimeAdapter {
    @Suppress("UndocumentedPublicFunction")
    @ToJson
    fun toJson(value: LocalTime): String = DateTimeFormatter.ISO_LOCAL_TIME.format(value)

    @Suppress("UndocumentedPublicFunction")
    @FromJson
    fun fromJson(value: String): LocalTime = LocalTime.parse(value, DateTimeFormatter.ISO_LOCAL_TIME)
}

/**
 * Custom adapter for using [LocalDate] with Moshi.
 */
class LocalDateAdapter {
    @Suppress("UndocumentedPublicFunction")
    @ToJson
    fun toJson(value: LocalDate): String = DateTimeFormatter.ISO_LOCAL_DATE.format(value)

    @Suppress("UndocumentedPublicFunction")
    @FromJson
    fun fromJson(value: String): LocalDate = LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE)
}