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

package com.hyperdevs.arch_example.network

import com.hyperdevs.arch_example.network.models.DisneyCharacterId
import com.hyperdevs.arch_example.network.models.DisneyPaginatedCharacters
import com.hyperdevs.arch_example.network.models.NetworkDisneyCharacter
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Class in charge of the communication with the Disney API using Retrofit.
 */
@Suppress("UndocumentedPublicFunction")
interface DisneyApi {

    @GET("/character")
    suspend fun getCharacters(@Query("page") pageNumber: Int): DisneyPaginatedCharacters

    @GET("/character/{id}")
    suspend fun getCharacterById(@Path("id") id: DisneyCharacterId): NetworkDisneyCharacter
}
