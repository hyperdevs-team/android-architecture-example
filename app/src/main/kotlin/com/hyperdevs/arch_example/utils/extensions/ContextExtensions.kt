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

package com.hyperdevs.arch_example.utils.extensions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import timber.log.Timber

/**
 * Displays a text as a toast in the current [Context].
 *
 * @param textRes Text to display in the toast as a string resource ID
 * @param duration Duration, one of [Toast.LENGTH_SHORT] or [Toast.LENGTH_LONG]
 */
fun Context.showToast(@StringRes textRes: Int, duration: Int = Toast.LENGTH_SHORT) =
    showToast(getString(textRes), duration)

/**
 * Displays a text as a toast in the current [Context].
 *
 * @param text Text to display in the toast
 * @param duration Duration, one of [Toast.LENGTH_SHORT] or [Toast.LENGTH_LONG]
 */
fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}

/**
 * Open the given [url] in a browser selected by the user or the default one.
 * Remember to add in the manifest the proper query:
 * <queries>
 *     <intent>
 *         <action android:name="android.intent.action.VIEW" />
 *     </intent>
 * </queries>
 */
fun Context.openInBrowser(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(Intent.createChooser(intent, null))
}

/**
 * Open the given [Uri] in a browser selected by the user or the default one.
 * Remember to add in the manifest the proper query:
 * <queries>
 *     <intent>
 *         <action android:name="android.intent.action.VIEW" />
 *     </intent>
 * </queries>
 */
fun Context.openInBrowser(uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW, uri)
    startActivity(Intent.createChooser(intent, null))
}

/**
 * Get the current application version.
 */
fun Context.getAppVersion(): String? =
    try {
        packageManager.getPackageInfo(packageName, 0).versionName.replace("[a-zA-Z]|-".toRegex(), "")
    } catch (e: PackageManager.NameNotFoundException) {
        Timber.e(e, "Error getting the app version name")
        null
    }

/**
 * Open an email app with the given [emailAddress] as destination, selected by the user or the default one.
 * Returns false if there isn't any installed app that can send the email or true otherwise.
 * Remember to add in the manifest the proper query:
 * <queries>
 *     <intent>
 *         <action android:name="android.intent.action.SENDTO" />
 *         <data android:scheme="mailto" />
 *     </intent>
 * </queries>
 */
fun Context.openEmailComposer(emailAddress: String? = null): Boolean {
    val selectorIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
    }
    val sendEmailIntent = Intent(Intent.ACTION_SENDTO).apply {
        emailAddress?.let { putExtra(Intent.EXTRA_EMAIL, arrayOf(it)) }
        selector = selectorIntent
    }
    return if (sendEmailIntent.resolveActivity(packageManager) != null) {
        startActivity(Intent.createChooser(sendEmailIntent, null))
        true
    } else {
        false
    }
}