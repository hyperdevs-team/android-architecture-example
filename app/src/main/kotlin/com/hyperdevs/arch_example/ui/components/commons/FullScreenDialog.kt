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

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.hyperdevs.arch_example.R
import com.google.accompanist.insets.LocalWindowInsets
import java.util.*
import kotlin.math.abs

@Suppress("MagicNumber")
private val DarkScrimColor = Color(0f, 0f, 0f, 0.3f)

/**
 * Full screen dialog that can be positioned in the screen's window given the gravity in [gravity].
 */
@Suppress("LongMethod", "ComplexMethod")
@Composable
fun FullScreenDialog(widthFraction: Float = 1f,
                     heightFraction: Float = 1f,
                     gravity: Int = Gravity.CENTER,
                     invalidateContent: Boolean = false,
                     showBehindStatusBar: Boolean = false,
                     showScrimBehind: Boolean = true,
                     onDismiss: () -> Unit,
                     content: @Composable () -> Unit) {

    val composeView = LocalView.current
    val context = ContextThemeWrapper(composeView.context, R.style.DialogWindowTheme)
    val composition = rememberCompositionContext()
    val statusBar = abs(LocalWindowInsets.current.statusBars.bottom - LocalWindowInsets.current.statusBars.top)
    val navigationBar = LocalWindowInsets.current.navigationBars.bottom - LocalWindowInsets.current.navigationBars.top

    val displayRealSize = Point()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = context.getSystemService(WindowManager::class.java).currentWindowMetrics
        displayRealSize.x = windowMetrics.bounds.width()
        displayRealSize.y = windowMetrics.bounds.height()
    } else {
        composeView.display.getRealSize(displayRealSize)
    }

    val dialogId = rememberSaveable { UUID.randomUUID() }

    // As the dialog isn't a view, we can't use an AndroidView for this
    val dialog = remember {
        Dialog(context).apply dialog@{
            setOnDismissListener { onDismiss() }

            val dialogLayout = DialogLayout(context, window!!).apply {
                // Set unique id for AbstractComposeView. This allows state restoration for the state
                // defined inside the Dialog via rememberSaveable()
                setTag(R.id.compose_view_saveable_id_tag, "Dialog:$dialogId")
            }

            dialogLayout.setAsComposeContentIn(this, composeView)

            val statusBarHeight = if (showBehindStatusBar) 0 else statusBar
            window?.apply {
                addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

                if (!showScrimBehind) {
                    setBackgroundDrawableResource(android.R.color.transparent)
                }
                attributes?.apply {
                    this.width = (displayRealSize.x * widthFraction).toInt()
                    this.height = (displayRealSize.y * heightFraction).toInt() - statusBarHeight - navigationBar
                    this.gravity = gravity
                    if (showBehindStatusBar) {
                        y = when (gravity and Gravity.VERTICAL_GRAVITY_MASK) {
                            Gravity.TOP -> -statusBar
                            Gravity.BOTTOM -> 0
                            else -> -statusBar / 2
                        }
                    }
                }
            }
        }
    }

    // Update the UI with the recomposition. We need to recreate the DialogLayout each time so it works properly
    val updatedDialogLayout: DialogLayout? = remember(invalidateContent) {
        dialog.window?.let {
            DialogLayout(context, it).apply {
                // Set unique id for AbstractComposeView. This allows state restoration for the state
                // defined inside the Dialog via rememberSaveable()
                setTag(R.id.compose_view_saveable_id_tag, "Dialog:$dialogId")
            }
        }
    }
    updatedDialogLayout?.setContent(composition) {
        Box {
            content()
            if (showBehindStatusBar && !showScrimBehind) {
                // Add black scrim to ensure the status bar icons are always seen correctly, as the system set them as
                // white, not matter what we set
                Box(
                    Modifier
                    .background(DarkScrimColor)
                    .fillMaxWidth()
                    .height(with(LocalDensity.current) { statusBar.toDp() })
                    .align(Alignment.TopCenter))
            }
        }
    }

    LaunchedEffect(updatedDialogLayout) {
        updatedDialogLayout?.setAsComposeContentIn(dialog, composeView)
    }

    DisposableEffect(dialog) {
        dialog.show()

        onDispose {
            dialog.dismiss()
        }
    }
}

private class DialogLayout(context: Context,
                           override val window: Window) : AbstractComposeView(context), DialogWindowProvider {

    private var content: @Composable () -> Unit by mutableStateOf({})

    fun setContent(parent: CompositionContext, content: @Composable () -> Unit) {
        setParentCompositionContext(parent)
        this.content = content
        createComposition()
    }

    @Composable
    override fun Content() {
        content()
    }

    fun setAsComposeContentIn(dialog: Dialog, composeView: View) {
        this.setViewTreeLifecycleOwner(composeView.findViewTreeLifecycleOwner())
        this.setViewTreeViewModelStoreOwner(composeView.findViewTreeViewModelStoreOwner())
        this.setViewTreeSavedStateRegistryOwner(composeView.findViewTreeSavedStateRegistryOwner())
        dialog.setContentView(this)
    }
}