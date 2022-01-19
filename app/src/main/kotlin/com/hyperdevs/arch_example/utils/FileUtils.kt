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

package com.hyperdevs.arch_example.utils

import android.content.Context
import android.net.Uri
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * Utils related to [File]s.
 */
object FileUtils {

    private const val CONTENT_RESOLVER_CURSOR_DATA_COLUMN = "_data"
    private const val TEMPORAL_FOLDER_NAME = "tmp"
    private const val BYTE_ARRAY_BUFFER_SIZE = 4096
    private const val FILE_READ_MODE = "r"

    /**
     * Returns the original path of a file passing its Uri.
     */
    @Suppress("NestedBlockDepth")
    fun getMediaPath(context: Context, uri: Uri): String {
        val resolver = context.contentResolver
        val projection = arrayOf(CONTENT_RESOLVER_CURSOR_DATA_COLUMN)
        return try {
            resolver.query(uri, projection, null, null, null)?.use {
                val columnIndex = it.getColumnIndexOrThrow(CONTENT_RESOLVER_CURSOR_DATA_COLUMN)
                it.moveToFirst()
                it.getString(columnIndex)
            } ?: throw IllegalArgumentException()
        } catch (e: Exception) {
            val file = getNewTemporalMediaFile(context, TEMPORAL_FOLDER_NAME)

            resolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    val buf = ByteArray(BYTE_ARRAY_BUFFER_SIZE)
                    var len: Int
                    while (inputStream.read(buf).also { len = it } > 0)
                        outputStream.write(buf, 0, len)
                }
            }
            file.absolutePath
        }
    }

    /**
     * Returns a file in the temporal files.
     */
    fun getNewTemporalMediaFile(context: Context, fileName: String): File =
        File.createTempFile(fileName, null, context.cacheDir)

    /**
     * Create a new temporal file in the local internal storage.
     */
    @Suppress("NestedBlockDepth")
    fun createNewTemporalMediaFile(context: Context,
                                   outputFileName: String,
                                   sourceContentUri: Uri,
                                   overwriteFileWithSameName: Boolean = false): File =
        File.createTempFile(outputFileName, null, context.cacheDir).apply {
            if (createNewFile() || overwriteFileWithSameName) {
                // Try to open the file
                context.contentResolver.openFileDescriptor(sourceContentUri, FILE_READ_MODE)?.use { inputFd ->
                    outputStream().use { fileOutputStream ->
                        FileInputStream(inputFd.fileDescriptor).use { fileInputStream ->
                            fileInputStream.copyTo(fileOutputStream)
                        }
                    }
                }
            } else {
                Timber.w("File already exists")
            }
        }

}