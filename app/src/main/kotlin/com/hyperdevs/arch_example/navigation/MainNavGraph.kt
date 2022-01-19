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

package com.hyperdevs.arch_example.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hyperdevs.arch_example.characters.ui.CharacterDetailsScreen
import com.hyperdevs.arch_example.characters.ui.CharacterDetailsViewModel
import com.hyperdevs.arch_example.characters.ui.CharactersScreen
import com.hyperdevs.arch_example.characters.ui.CharactersViewModel
import mini.kodein.android.compose.viewModel
import org.kodein.di.compose.androidContextDI

@Suppress("UndocumentedPublicFunction")
@Composable
fun MainNavGraph(
    modifier: Modifier,
    navController: NavHostController = rememberNavController(),
    startDestinationScreenType: ScreenType = CharactersScreenType.Characters
) {
    NavHost(navController, startDestination = startDestinationScreenType.routeDefinition) {
        addCharacterScreens(navController, modifier)
    }
}

private fun NavGraphBuilder.addCharacterScreens(navController: NavController, modifier: Modifier) {
    composable(route = CharactersScreenType.Characters.routeDefinition) {
        val charactersViewModel: CharactersViewModel by it.viewModel(androidContextDI())
        CharactersScreen(modifier, charactersViewModel) { characterId ->
            navController.navigate(CharactersScreenType.CharacterDetails.getRouteFor(characterId))
        }
    }

    composable(
        route = CharactersScreenType.CharacterDetails.routeDefinition,
        arguments = listOf(navArgument(CharactersScreenType.CharacterDetails.CHARACTER_ID_ARG) {
            type = NavType.IntType
        })
    ) { backStackEntry ->
        val characterId = backStackEntry.arguments?.getInt(CharactersScreenType.CharacterDetails.CHARACTER_ID_ARG)
        // TODO: Don't force the not null and show a proper error
        val characterDetailsViewModel: CharacterDetailsViewModel
            by backStackEntry.viewModel(androidContextDI(), characterId!!)
        CharacterDetailsScreen(modifier, characterDetailsViewModel) {
            navController.popBackStack()
        }
    }
}