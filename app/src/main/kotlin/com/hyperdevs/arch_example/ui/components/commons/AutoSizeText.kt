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

package com.hyperdevs.arch_example.ui.components.commons

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import com.hyperdevs.arch_example.utils.extensions.indexesOf
import com.hyperdevs.arch_example.utils.extensions.splitWords

/**
 * Custom [Text] that adapts the font size depending on the width of the view.
 */
@Suppress("MagicNumber")
@Composable
fun AutoSizeText(text: String,
                 style: TextStyle,
                 color: Color = MaterialTheme.colors.onBackground,
                 maxLines: Int,
                 modifier: Modifier = Modifier) {

    val scaledTextStyle = remember { mutableStateOf(style) }
    val readyToDraw = remember { mutableStateOf(false) }
    val wordsOfText = text.splitWords().distinct()
    // Used for managing the same word duplicated in the phrase
    val wordsOfTextWithIndexes = wordsOfText.associateWith { text.indexesOf(it) }

    Text(
        text = text,
        modifier = modifier.drawWithContent {
            if (readyToDraw.value) {
                drawContent()
            }
        },
        style = scaledTextStyle.value,
        color = color,
        softWrap = true,
        maxLines = maxLines,
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.hasVisualOverflow
                || isAnyWordSplitInDifferentLines(wordsOfTextWithIndexes, textLayoutResult)) {
                scaledTextStyle.value = scaledTextStyle.value.copy(fontSize = scaledTextStyle.value.fontSize * 0.9)
            } else {
                readyToDraw.value = true
            }
        }
    )
}

private fun isAnyWordSplitInDifferentLines(wordsOfTextWithIndexes: Map<String, List<Int>>,
                                           textLayoutResult: TextLayoutResult) =
    wordsOfTextWithIndexes.any { (word, wordIndexes) ->
        wordIndexes.any { wordIndex ->
            val firstLetterLine = textLayoutResult.getLineForOffset(wordIndex)
            val lastLetterLine = textLayoutResult.getLineForOffset(wordIndex + word.lastIndex)
            firstLetterLine != lastLetterLine
        }
    }