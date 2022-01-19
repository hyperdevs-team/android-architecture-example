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

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.REPEAT_MODE_ONE
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util
import com.hyperdevs.arch_example.utils.extensions.isTablet
import timber.log.Timber

/**
 * Simple video player.
 */
@Composable
fun SimpleVideoPlayer(modifier: Modifier,
                      uri: Uri,
                      playVideo: Boolean,
                      isAppInBackground: Boolean,
                      repeatInfinitely: Boolean = true,
                      scaleModeCrop: Boolean = true,
                      showUserController: Boolean = false,
                      playerListener: Player.Listener? = null) {
    val context = LocalContext.current

    val exoPlayer = remember(uri) { ExoPlayer.Builder(context).build() }

    DisposableEffect(uri) {
        if (!isAppInBackground) {
            val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(
                context, DefaultHttpDataSource.Factory().setUserAgent(Util.getUserAgent(context, context.packageName))
            )

            val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri))

            exoPlayer.apply {
                setMediaSource(source)
                if (repeatInfinitely) {
                    repeatMode = REPEAT_MODE_ONE
                }
                playerListener?.let {
                    addListener(it)
                }
                if (playVideo) {
                    exoPlayer.playWhenReady = true
                }
                prepare()
            }
        }

        onDispose {
            exoPlayer.stop()
            exoPlayer.release()
        }
    }

    DisposableEffect(playVideo, isAppInBackground) {
        if (playVideo && !isAppInBackground) {
            exoPlayer.playWhenReady = true
        }

        onDispose {
            // This is called when [playVideo] is going to change its value, but it haven't done yet, so here [playVideo]
            // has the old value, the one with this [DisposableEffect] was set previously
            if (playVideo && !isAppInBackground) {
                try {
                    exoPlayer.pause()
                    exoPlayer.seekToDefaultPosition()
                } catch (e: Exception) {
                    Timber.e(e, "Error pausing the video player")
                }
            }
        }
    }

    SimplePlayerView(
        modifier = modifier,
        exoPlayer = exoPlayer,
        isAppInBackground = isAppInBackground,
        scaleModeCrop = scaleModeCrop,
        showUserController = showUserController
    )
}

@Composable
private fun SimplePlayerView(modifier: Modifier,
                             exoPlayer: ExoPlayer,
                             isAppInBackground: Boolean,
                             scaleModeCrop: Boolean = true,
                             showUserController: Boolean = false) {
    val isTablet = LocalConfiguration.current.isTablet()
    val context = LocalContext.current
    val playerView = remember(isAppInBackground) {
        PlayerView(context).apply {
            useController = showUserController
        }
    }
    AndroidView(
        modifier = modifier,
        factory = {
            playerView
        }
    ) {
        it.player = exoPlayer
        if (scaleModeCrop) {
            it.resizeMode = if (isTablet) RESIZE_MODE_FIXED_WIDTH else RESIZE_MODE_FIXED_HEIGHT
            exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        }
    }
}