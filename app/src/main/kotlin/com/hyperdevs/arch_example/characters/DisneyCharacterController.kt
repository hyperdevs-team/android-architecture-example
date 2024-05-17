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

package com.hyperdevs.arch_example.characters

import android.content.Context
import androidx.paging.*
import com.hyperdevs.arch_example.connectivity.ConnectivityUtils
import com.hyperdevs.arch_example.connectivity.NetworkUnavailableException
import com.hyperdevs.arch_example.network.DisneyApi
import com.hyperdevs.arch_example.network.models.DisneyCharacter
import com.hyperdevs.arch_example.network.models.DisneyCharacterId
import com.hyperdevs.arch_example.network.models.toDisneyCharacter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import mini.Resource

/**
 * Interface that Disney controllers must comply to in order to work.
 */
interface DisneyCharacterController {

    /**
     * Get a list of Disney characters from a REST API.
     */
    suspend fun getDisneyCharactersPaginatedStream(): Flow<PagingData<DisneyCharacter>>

    /**
     * Get the details of a Disney character given its id from a REST API.
     */
    suspend fun getDisneyCharacter(characterId: DisneyCharacterId): Resource<DisneyCharacter>
}

/**
 * Implementation for [DisneyCharacterController] using a REST API as backend.
 */
class DisneyCharacterControllerImpl(private val disneyApi: DisneyApi,
                                    private val fakeDisneyApi: DisneyApi,
                                    private val context: Context) : DisneyCharacterController {

    companion object {
        private const val CHARACTERS_PER_PAGE_COUNT = 15
        private const val INITIAL_PAGE_NUMBER = 1
        private const val NEXT_PAGE_STEP = 1
    }

    override suspend fun getDisneyCharactersPaginatedStream(): Flow<PagingData<DisneyCharacter>> =
        Pager(PagingConfig(pageSize = CHARACTERS_PER_PAGE_COUNT)) {
            object : PagingSource<Int, DisneyCharacter>() {
                override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DisneyCharacter> =
                    withContext(Dispatchers.IO) {
                        try {
                            val pageNumber = params.key ?: INITIAL_PAGE_NUMBER
                            val disneyCharactersData = disneyApi.getCharacters(pageNumber)
                            val disneyCharacters = disneyCharactersData.data.map { it.toDisneyCharacter() }
                            val isLastPage = disneyCharactersData.info.nextPage.isNullOrBlank()
                            LoadResult.Page(
                                data = disneyCharacters,
                                prevKey = if (pageNumber == INITIAL_PAGE_NUMBER) null else pageNumber - NEXT_PAGE_STEP,
                                nextKey = if (!isLastPage) pageNumber.plus(NEXT_PAGE_STEP) else null
                            )
                        } catch (e: Exception) {
                            val error =
                                if (!ConnectivityUtils.hasInternetConnection(context)) NetworkUnavailableException
                                else e
                            LoadResult.Error(error)
                        }
                    }

                override fun getRefreshKey(state: PagingState<Int, DisneyCharacter>): Int? =
                    state.anchorPosition?.let { anchorPosition ->
                        state.closestPageToPosition(anchorPosition)?.prevKey?.plus(NEXT_PAGE_STEP)
                            ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(NEXT_PAGE_STEP)
                    }
            }
        }.flow

    override suspend fun getDisneyCharacter(characterId: DisneyCharacterId): Resource<DisneyCharacter> =
        try {
            val disneyCharacter = disneyApi.getCharacterById(characterId).toDisneyCharacter()
            Resource.success(disneyCharacter)
        } catch (e: Exception) {
            Resource.failure(e)
        }

}