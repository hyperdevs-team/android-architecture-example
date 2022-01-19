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

package com.hyperdevs.arch_example.characters.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperdevs.arch_example.BaseTypedViewModel
import com.hyperdevs.arch_example.characters.DisneyCharacterState
import com.hyperdevs.arch_example.characters.DisneyCharacterStore
import com.hyperdevs.arch_example.characters.GetDisneyCharacterDetailsAction
import com.hyperdevs.arch_example.network.models.DisneyCharacter
import com.hyperdevs.arch_example.network.models.DisneyCharacterId
import com.hyperdevs.arch_example.utils.CharacterNotFoundException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import mini.*
import org.kodein.di.instance

/**
 * [ViewModel] used to represent a given Disney characters data.
 */
class CharacterDetailsViewModel(app: Application,
                                private val characterId: Int) : BaseTypedViewModel<Int>(app, characterId) {

    private val dispatcher: Dispatcher by instance()
    private val disneyCharacterStore: DisneyCharacterStore by instance()

    private val _disneyCharacter =
        MutableStateFlow<Resource<CharacterDetailsViewData>>(Resource.loading())
    val disneyCharacter: StateFlow<Resource<CharacterDetailsViewData>> get() = _disneyCharacter

    private val _isCharacterFavorite = MutableStateFlow(false)
    val isCharacterFavorite: StateFlow<Boolean> get() = _isCharacterFavorite

    init {
        if (disneyCharacterStore.state.getDisneyCharacterDetailsTasks[characterId]?.isSuccess != true) {
            refresh()
        }

        disneyCharacterStore.flow()
            .select { CharacterDetailsViewData.from(it, characterId) }
            .onEach { _disneyCharacter.value = it }
            .launchIn(viewModelScope)
    }

    /**
     * Refresh the info of the character.
     */
    fun refresh() {
        viewModelScope.launch {
            dispatcher.dispatch(GetDisneyCharacterDetailsAction(characterId))
        }
    }

    /**
     * Mark the current Disney character as one of the user's favorites.
     */
    fun addAsFavorite() {
        // TODO: Proper implementation would call to our database or API to set the value
        _isCharacterFavorite.value = true
    }

    /**
     * Remove the current Disney character from the user's favorites.
     */
    fun removeFromFavorite() {
        // TODO: Proper implementation would call to our database or API to set the value
        _isCharacterFavorite.value = false
    }
}

@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
data class CharacterDetailsViewData(val disneyCharacter: DisneyCharacter) {
    companion object {
        fun from(state: DisneyCharacterState,
                 characterId: DisneyCharacterId): Resource<CharacterDetailsViewData> = with(state) {

            val characterDetailsTask = getDisneyCharacterDetailsTasks[characterId]
                ?: return@with Resource.failure(CharacterNotFoundException(characterId))
            when {
                characterDetailsTask.isSuccess -> {
                    val character = disneyCharacterDetails[characterId]
                    if (character != null) {
                        Resource.success(CharacterDetailsViewData(character))
                    } else {
                        Resource.failure()
                    }
                }
                characterDetailsTask.isFailure -> Resource.failure(getDisneyCharactersTask.exceptionOrNull())
                characterDetailsTask.isLoading -> Resource.loading()
                else -> Resource.idle()
            }
        }
    }
}