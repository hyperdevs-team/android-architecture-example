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

import androidx.paging.PagingData
import com.hyperdevs.arch_example.network.NetworkModule
import com.hyperdevs.arch_example.network.models.DisneyCharacter
import com.hyperdevs.arch_example.network.models.DisneyCharacterId
import com.hyperdevs.arch_example.utils.extensions.toTask
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import mini.Dispatcher
import mini.Reducer
import mini.State
import mini.Store
import mini.Task
import mini.kodein.bindStore
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

@Suppress("UndocumentedPublicClass")
data class DisneyCharacterState(val getDisneyCharactersTask: Task = Task.idle(),
                                val getDisneyCharacterDetailsTasks: Map<DisneyCharacterId, Task> = emptyMap(),
                                val disneyCharactersPaginatedStream: Flow<PagingData<DisneyCharacter>>? = null,
                                val disneyCharacterDetails: Map<DisneyCharacterId, DisneyCharacter> = emptyMap())
    :State {

    override fun toString(): String = "DisneyState(" +
        "disneyCharactersTask=$getDisneyCharactersTask, " +
        "getDisneyCharacterDetailsTasks=$getDisneyCharacterDetailsTasks, " +
        "disneyCharacterDetails=${disneyCharacterDetails.mapValues { it.value.name }})"
}

/**
 * Store in charge of handle Disney state during the app's lifecycle.
 */
@Suppress("UndocumentedPublicFunction")
class DisneyCharacterStore(private val disneyCharacterController: DisneyCharacterController,
                           private val dispatcher: Dispatcher) : Store<DisneyCharacterState>() {

    /*
     * Manage the [CancellationException] so when the user moves to another screen, and the view model which dispatched
     * this action is cleared and all its coroutines are cancelled, if this process has not finished yet, we reset its
     * task to [Task.idle()]. This is needed because if not it would remain as [Task.loading()] forever, and would
     * prevent to be executed again if this action is dispatched again because it would be already `isLoading` state.
     */
    @Reducer
    suspend fun getDisneyCharactersList(action: GetDisneyCharactersAction) {
        if (state.getDisneyCharactersTask.isLoading) return
        setState(state.copy(getDisneyCharactersTask = Task.loading()))
        try {
            dispatcher.dispatch(OnTaskLoadingAction("disneyCharactersTask"))

            val charactersPagedStream = disneyCharacterController.getDisneyCharactersPaginatedStream()
            setState(
                state.copy(
                    disneyCharactersPaginatedStream = charactersPagedStream,
                    getDisneyCharactersTask = Task.success()
                )
            )
        } catch (e: CancellationException) {
            setState(state.copy(getDisneyCharactersTask = Task.idle()))
        }
    }

    @Reducer
    suspend fun getDisneyCharacterDetails(action: GetDisneyCharacterDetailsAction) {
        if (state.getDisneyCharacterDetailsTasks[action.characterId]?.isLoading == true) return
        setState(
            state.copy(
                getDisneyCharacterDetailsTasks =
                state.getDisneyCharacterDetailsTasks.plus(action.characterId to Task.loading())
            )
        )
        try {
            dispatcher.dispatch(OnTaskLoadingAction("getDisneyCharacterDetailsTask[${action.characterId}]"))

            val characterResource = disneyCharacterController.getDisneyCharacter(action.characterId)
            val newCharacterDetails = characterResource.getOrNull()?.let {
                state.disneyCharacterDetails.plus(it.id to it)
            }
            setState(
                state.copy(
                    disneyCharacterDetails = newCharacterDetails ?: state.disneyCharacterDetails,
                    getDisneyCharacterDetailsTasks =
                    state.getDisneyCharacterDetailsTasks.plus(action.characterId to characterResource.toTask())
                )
            )
        } catch (e: CancellationException) {
            setState(
                state.copy(
                    getDisneyCharacterDetailsTasks =
                    state.getDisneyCharacterDetailsTasks.plus(action.characterId to Task.idle())
                )
            )
        }
    }

}

@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
object DisneyModule {
    fun create() = DI.Module("DisneyModule") {
        bindStore { DisneyCharacterStore(instance(), instance()) }
        bind<DisneyCharacterController>() with singleton {
            DisneyCharacterControllerImpl(instance(), instance(NetworkModule.DISNEY_FAKE_API_TAG), instance())
        }
    }
}