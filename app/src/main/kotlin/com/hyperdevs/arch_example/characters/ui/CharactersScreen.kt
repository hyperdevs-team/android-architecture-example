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

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyGridScope
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Constraints
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.hyperdevs.arch_example.R
import com.hyperdevs.arch_example.network.models.DisneyCharacter
import com.hyperdevs.arch_example.ui.components.ErrorScreen
import com.hyperdevs.arch_example.ui.components.GenericErrorDialog
import com.hyperdevs.arch_example.ui.components.commons.AppTopBar
import com.hyperdevs.arch_example.ui.theme.Dimens
import com.hyperdevs.arch_example.ui.theme.ResValues
import com.hyperdevs.arch_example.utils.extensions.items
import com.hyperdevs.arch_example.utils.extensions.showToast

private const val LOADING_CHARACTERS_COUNT = 8

@Composable
@Suppress("UndocumentedPublicFunction")
fun CharactersScreen(modifier: Modifier,
                     viewModel: CharactersViewModel,
                     navigateToCharacterDetails: (characterId: Int) -> Unit) {

    val charactersStream = viewModel.disneyCharactersStream?.collectAsLazyPagingItems()

    Scaffold(
        modifier = modifier.statusBarsPadding(),
        topBar = {
            AppTopBar(
                titleRes = R.string.app_name,
                backgroundColor = MaterialTheme.colors.primaryVariant,
                actions = {
                    val localContext = LocalContext.current
                    val searchActionText = stringResource(R.string.top_app_bar_search)
                    IconButton(
                        onClick = {
                            // TODO: Navigate to another screen or do a call to the view model, etc
                            localContext.showToast(searchActionText)
                        }
                    ) {
                        Icon(Icons.Outlined.Search, contentDescription = "")
                    }
                }
            )
        }
    ) {
        Characters(charactersStream, navigateToCharacterDetails)
    }
}

@Composable
private fun Characters(charactersStream: LazyPagingItems<DisneyCharacter>?,
                       onCharacterClick: (characterId: Int) -> Unit) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val parentMaxWidth = constraints.maxWidth
        val numberOfColumns = ResValues.general_grid_cells

        // TODO: Until this issue is done, we need to calculate the proper width for items that occupy all the width not
        //  just one cell https://issuetracker.google.com/u/1/issues/176758183
        val fullWidthItemMeasurable: MeasureScope.(Measurable, Constraints) -> MeasureResult = remember {
            { measurable, constraints ->
                val placeable = measurable.measure(
                    constraints.copy(
                        maxWidth = parentMaxWidth,
                    )
                )
                layout(constraints.maxWidth, placeable.measuredHeight) {
                    placeable.placeRelative(0, 0)
                }
            }
        }
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            cells = GridCells.Fixed(ResValues.general_grid_cells),
            contentPadding = PaddingValues(Dimens.generalDimens.layout_padding),
            verticalArrangement = Arrangement.spacedBy(Dimens.characterDimens.card_items_space_in_grid),
            horizontalArrangement = Arrangement.spacedBy(Dimens.characterDimens.card_items_space_in_grid)
        ) {
            if (charactersStream != null) {
                showCharacters(charactersStream, numberOfColumns, fullWidthItemMeasurable, onCharacterClick)
            } else {
                items(LOADING_CHARACTERS_COUNT) {
                    CharacterCard(contentIsLoading = true)
                }
            }
        }
    }
}

@Suppress("LongMethod")
private fun LazyGridScope.showCharacters(charactersStream: LazyPagingItems<DisneyCharacter>,
                                         numberOfColumns: Int,
                                         fullWidthItemMeasurable:
                                         MeasureScope.(Measurable, Constraints) -> MeasureResult,
                                         onCharacterClick: (characterId: Int) -> Unit) {

    with(charactersStream) {
        when {
            loadState.append is LoadState.NotLoading
                && loadState.refresh is LoadState.NotLoading
                && loadState.prepend is LoadState.NotLoading
                && loadState.prepend.endOfPaginationReached
                && itemCount < 1 -> {
                item {
                    ErrorScreen(
                        errorTextRes = R.string.get_characters_list_error_message,
                        modifier = Modifier
                            .layout(fullWidthItemMeasurable)
                            .fillMaxWidth()
                    )
                }
            }
            loadState.refresh is LoadState.Loading -> {
                items(LOADING_CHARACTERS_COUNT) {
                    CharacterCard(contentIsLoading = true)
                }
            }
            else -> {
                items(this) {
                    CharacterCard(character = it) { characterId ->
                        onCharacterClick(characterId)
                    }
                }

                when {
                    loadState.append is LoadState.Loading -> {
                        // TODO: Until this issue is done, we need to pass the remaining cell items as empty, to complete the row
                        //       https://issuetracker.google.com/u/1/issues/176758183
                        repeat(numberOfColumns - charactersStream.itemCount % numberOfColumns) {
                            item { }
                        }
                        item {
                            Row(
                                modifier = Modifier
                                    .layout(fullWidthItemMeasurable)
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = Dimens.characterDimens.loading_more_characters_icon_padding_vertical,
                                    ),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    loadState.refresh is LoadState.Error -> {
                        item {
                            ErrorScreen(
                                errorTextRes = R.string.get_characters_list_error_message,
                                modifier = Modifier
                                    .layout(fullWidthItemMeasurable)
                                    .fillMaxWidth()
                            )
                        }
                    }
                    loadState.append is LoadState.Error -> {
                        item {
                            GenericErrorDialog {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Suppress("UndocumentedPublicFunction", "MagicNumber")
fun CharacterCard(character: DisneyCharacter? = null,
                  contentIsLoading: Boolean = false,
                  onCharacterClick: (characterId: Int) -> Unit = {}) {
    Card(
        modifier = Modifier
            .let { if (contentIsLoading) it.height(Dimens.characterDimens.card_loading_height) else it }
            .placeholder(
                visible = contentIsLoading,
                highlight = PlaceholderHighlight.shimmer()
            ),
        elevation = Dimens.characterDimens.card_elevation
    ) {
        character?.let {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onCharacterClick(it.id) }
                    .padding(Dimens.characterDimens.card_internal_padding)
            ) {
                Image(
                    painter = rememberImagePainter(it.imageUrl),
                    contentDescription = stringResource(R.string.character_image_content_description, it.name),
                    contentScale = ContentScale.Crop,            // Crop the image if it's not a square
                    modifier = Modifier
                        .size(Dimens.characterDimens.card_image_size)
                        .clip(CircleShape)
                        .border(Dimens.characterDimens.card_image_border, Color.Gray, CircleShape)
                )
                Text(
                    text = it.name,
                    style = MaterialTheme.typography.h5,
                    fontFamily = FontFamily.Cursive,
                    modifier = Modifier.padding(
                        horizontal = Dimens.characterDimens.card_text_padding_horizontal,
                        vertical = Dimens.characterDimens.card_text_padding_vertical
                    )
                )
            }
        }
    }
}