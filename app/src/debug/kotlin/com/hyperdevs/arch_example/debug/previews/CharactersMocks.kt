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

package com.hyperdevs.arch_example.debug.previews

import com.hyperdevs.arch_example.network.models.NetworkDisneyCharacter
import com.hyperdevs.arch_example.network.models.NetworkDisneyCharacterData
import com.hyperdevs.arch_example.network.models.NetworkDisneyCharacterInfo

@Suppress("UndocumentedPublicClass", "MaxLineLength")
object CharactersMocks {
    @Suppress("UndocumentedPublicFunction")
    fun getCharactersMock() =
        listOf(
            NetworkDisneyCharacter(
                info = NetworkDisneyCharacterInfo(
                  count = 1,
                ),
                data = NetworkDisneyCharacterData(
                    name = "Queen Arianna",
                    imageUrl =
                "https://static.wikia.nocookie.net/disney/images/1/15/Arianna_Tangled.jpg/revision/latest?cb=20160715191802",
                    films = listOf("Tangled", "Tangled: Before Ever After")
                )
            ),
            NetworkDisneyCharacter(
                info = NetworkDisneyCharacterInfo(
                    count = 1,
                ),
                data = NetworkDisneyCharacterData(
                    name = "Queen Arianna",
                    imageUrl =
                    "https://static.wikia.nocookie.net/disney/images/1/15/Arianna_Tangled.jpg/revision/latest?cb=20160715191802",
                    films = listOf("Tangled", "Tangled: Before Ever After")
                )
            )
        )
}