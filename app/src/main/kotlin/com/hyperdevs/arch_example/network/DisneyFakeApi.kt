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

import com.hyperdevs.arch_example.debug.previews.CharactersMocks
import com.hyperdevs.arch_example.network.models.DisneyCharacterId
import com.hyperdevs.arch_example.network.models.DisneyPaginatedCharacters
import com.hyperdevs.arch_example.network.models.NetworkDisneyCharacter
import kotlinx.coroutines.delay

/**
 * Fake class for using while developing to get mocked data.
 */
class DisneyFakeApi: DisneyApi {
    override suspend fun getCharacters(pageNumber: Int): DisneyPaginatedCharacters {
        mockServerWork()
        val characters = CharactersMocks.getCharactersMock()
        return DisneyPaginatedCharacters(
            count = characters.size,
            nextPage = "2",
            previousPage = null,
            data = characters
        )
    }

    override suspend fun getCharacterById(id: DisneyCharacterId): NetworkDisneyCharacter {
        mockServerWork()
        return CharactersMocks.getCharactersMock().first()
    }

    /**
     * Random delay of 1-3 seconds to simulate server work.
     */
    @Suppress("MagicNumber")
    private suspend fun mockServerWork() = delay((1000L..3000L).random())

}
