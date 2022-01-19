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

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.hyperdevs.arch_example.R
import com.hyperdevs.arch_example.characters.ui.Character
import com.hyperdevs.arch_example.characters.ui.CharacterCard
import com.hyperdevs.arch_example.network.models.toDisneyCharacter
import com.hyperdevs.arch_example.ui.components.ErrorScreen
import com.hyperdevs.arch_example.ui.theme.AndroidArchitectureExampleTheme

@Preview(name = "Full screen error", showBackground = true)
@Composable
private fun ErrorPreview() {
    AndroidArchitectureExampleTheme {
        ErrorScreen(R.string.get_characters_list_error_message)
    }
}

@Preview(name = "Character list row", showBackground = true)
@Composable
private fun CharacterRowPreview() {
    AndroidArchitectureExampleTheme {
        CharacterCard(CharactersMocks.getCharactersMock().first().toDisneyCharacter()) {}
    }
}

@Preview(name = "Character", showBackground = true)
@Composable
private fun CharacterPreview() {
    AndroidArchitectureExampleTheme {
        Character(CharactersMocks.getCharactersMock().first().toDisneyCharacter(), {})
    }
}

@Preview(name = "Character - tablet", device = Devices.NEXUS_10, showBackground = true)
@Composable
private fun CharacterPreviewTablet() {
    AndroidArchitectureExampleTheme {
        Character(CharactersMocks.getCharactersMock().first().toDisneyCharacter(), {})
    }
}