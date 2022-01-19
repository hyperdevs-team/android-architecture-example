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
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hyperdevs.arch_example.BaseViewModel
import com.hyperdevs.arch_example.characters.DisneyCharacterStore
import com.hyperdevs.arch_example.characters.GetDisneyCharactersAction
import com.hyperdevs.arch_example.network.models.DisneyCharacter
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mini.Dispatcher
import mini.flow
import mini.select
import org.kodein.di.instance

/**
 * [ViewModel] used to represent a given Disney characters data.
 */
class CharactersViewModel(app: Application) : BaseViewModel(app) {

    private val dispatcher: Dispatcher by instance()
    private val disneyCharacterStore: DisneyCharacterStore by instance()

    var disneyCharactersStream: Flow<PagingData<DisneyCharacter>>? = null
        private set

    init {
        getDisneyCharacters()
    }

    /**
     * Ask for a Disney characters list and saved it in a [StateFlow] to be shown in the UI.
     */
    private fun getDisneyCharacters() {
        viewModelScope.launch {
            dispatcher.dispatch(GetDisneyCharactersAction)
        }

        disneyCharacterStore.flow()
            .select { it.disneyCharactersPaginatedStream }
            .take(1)
            .onEach { disneyCharactersStream = it?.cachedIn(viewModelScope) }
            .launchIn(viewModelScope)
    }
}