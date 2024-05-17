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

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import com.hyperdevs.arch_example.R
import com.hyperdevs.arch_example.network.models.DisneyCharacter
import com.hyperdevs.arch_example.ui.components.ErrorScreen
import com.hyperdevs.arch_example.ui.components.commons.AppTopBar
import com.hyperdevs.arch_example.ui.components.commons.ImageFromUrl
import com.hyperdevs.arch_example.ui.components.commons.ScrimCircularProgressIndicator
import com.hyperdevs.arch_example.ui.theme.Dimens
import com.hyperdevs.arch_example.utils.extensions.showToast

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@Suppress("UndocumentedPublicFunction")
fun CharacterDetailsScreen(modifier: Modifier,
                           characterDetailsViewModel: CharacterDetailsViewModel,
                           onNavigateUp: () -> Unit) {

    val characterResource = characterDetailsViewModel.disneyCharacter.collectAsState().value
    val isFavorite = characterDetailsViewModel.isCharacterFavorite.collectAsState().value

    Scaffold(
        modifier = modifier.statusBarsPadding(),
        topBar = {
            AppTopBar(
                title = characterResource.getOrNull()?.disneyCharacter?.name ?: stringResource(R.string.app_name),
                navigationIcon = rememberVectorPainter(Icons.Rounded.ArrowBack),
                navigationIconPaddingValues = PaddingValues(
                    start = Dimens.generalDimens.top_bar_navigation_icon_padding_start
                ),
                onNavigationIconClick = onNavigateUp,
                backgroundColor = MaterialTheme.colors.primaryVariant,
                actions = {
                    val localContext = LocalContext.current
                    val favoriteActionText = stringResource(R.string.top_app_bar_favorite)
                    IconButton(
                        onClick = {
                            if (!isFavorite) {
                                localContext.showToast(favoriteActionText)
                                characterDetailsViewModel.addAsFavorite()
                            } else {
                                characterDetailsViewModel.removeFromFavorite()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) {
        Box(Modifier.fillMaxSize()) {
            when {
                characterResource.isLoading -> {
                        ScrimCircularProgressIndicator(Modifier.fillMaxSize())
                    }
                characterResource.isSuccess -> {
                        Character(character = characterResource.getOrNull()?.disneyCharacter!!) {
                            characterDetailsViewModel.refresh()
                        }
                }
                characterResource.isFailure -> {
                    ErrorScreen(R.string.character_error_message)
                }
            }
        }
    }
}

@Composable
@Suppress("UndocumentedPublicFunction")
fun Character(character: DisneyCharacter, onRefreshCharacter: () -> Unit) {

    Column(modifier = Modifier.fillMaxSize()) {
        CharacterInfo(Modifier.weight(1f), character)
        Button(
            onClick = onRefreshCharacter,
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(
                    start = Dimens.generalDimens.layout_padding,
                    end = Dimens.generalDimens.layout_padding,
                    top = Dimens.characterDimens.details_refresh_button_padding_top
                )
        ) {
            Text(stringResource(id = R.string.refresh))
        }
    }
}

@Composable
private fun CharacterInfo(modifier: Modifier, character: DisneyCharacter) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = Dimens.generalDimens.layout_padding)
    ) {
        item {
            CharacterImage(character)
            Spacer(Modifier.height(Dimens.characterDimens.details_title_padding_top))
            Text(
                text = character.name,
                style = MaterialTheme.typography.h3,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.generalDimens.layout_padding)
            )
            Spacer(Modifier.height(Dimens.characterDimens.details_title_padding_bottom))
        }

        if (character.films.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(id = R.string.character_films),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(
                        horizontal = Dimens.generalDimens.layout_padding,
                        vertical = Dimens.characterDimens.details_films_title_padding_vertical
                    )
                )
                Spacer(Modifier.height(Dimens.characterDimens.details_film_padding_vertical))
            }
            items(items = character.films) {
                Text(
                    text = it,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(
                        horizontal = Dimens.characterDimens.details_film_padding_horizontal,
                        vertical = Dimens.characterDimens.details_film_padding_vertical
                    )
                )
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun CharacterImage(character: DisneyCharacter) {
    BoxWithConstraints {
        val imageHeight =
            LocalConfiguration.current.screenHeightDp * Dimens.characterDimens.details_image_height_ratio
        val imageModifier = Modifier
            .fillMaxWidth()
            .height(imageHeight.dp)
        if (character.imageUrl != null) {
            ImageFromUrl(
                imageUrl = character.imageUrl,
                placeholderRes = R.drawable.ic_mickey_mouse,
                contentDescription = stringResource(
                    R.string.character_image_content_description,
                    character.name
                ),
                modifier = imageModifier
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_mickey_mouse),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = imageModifier
            )
        }
    }
}