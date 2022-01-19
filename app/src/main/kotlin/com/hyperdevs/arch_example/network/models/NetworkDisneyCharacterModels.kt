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

/**
 * Object representing the Disney API response.
 */
data class DisneyPaginatedCharacters(
    val count: Int,
    val nextPage: String?,
    val previousPage: String?,
    val data: List<NetworkDisneyCharacter>
)

/**
 * Object representing the Disney Character inside the API response.
 */
@Suppress("ConstructorParameterNaming")
data class NetworkDisneyCharacter(
    val __v: Int = -1,
    val _id: DisneyCharacterId = -1,
    val allies: List<String> = emptyList(),
    val createdAt: String? = null,
    val enemies: List<String> = emptyList(),
    val films: List<String> = emptyList(),
    val imageUrl: String? = null,
    val name: String = "",
    val parkAttractions: List<String> = emptyList(),
    val shortFilms: List<String> = emptyList(),
    val sourceUrl: String? = null,
    val tvShows: List<String> = emptyList(),
    val updatedAt: String? = null,
    val url: String? = null,
    val videoGames: List<String> = emptyList()
)

@Suppress("UndocumentedPublicFunction")
fun NetworkDisneyCharacter.toDisneyCharacter() =
    DisneyCharacter(
        id = _id,
        allies = allies,
        createdAt = createdAt,
        enemies = enemies,
        films = films,
        imageUrl = imageUrl,
        name = name,
        parkAttractions = parkAttractions,
        shortFilms = shortFilms,
        sourceUrl = sourceUrl,
        tvShows = tvShows,
        updatedAt = updatedAt,
        url = url,
        videoGames = videoGames
    )