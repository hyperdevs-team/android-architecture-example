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

package com.hyperdevs.arch_example.network.models

typealias DisneyCharacterId = Int

/**
 * Object representing the Disney Character inside the API response.
 */
@Suppress("ConstructorParameterNaming")
data class DisneyCharacter(
    val id: DisneyCharacterId,
    val allies: List<String>,
    val createdAt: String?,
    val enemies: List<String>,
    val films: List<String>,
    val imageUrl: String?,
    val name: String,
    val parkAttractions: List<String>,
    val shortFilms: List<String>,
    val sourceUrl: String?,
    val tvShows: List<String>,
    val updatedAt: String?,
    val url: String?,
    val videoGames: List<String> = emptyList()
)