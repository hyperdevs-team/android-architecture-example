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

package com.hyperdevs.arch_example.network

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.hyperdevs.arch_example.BuildConfig
import com.hyperdevs.arch_example.utils.LocalDateAdapter
import com.hyperdevs.arch_example.utils.LocalDateTimeAdapter
import com.hyperdevs.arch_example.utils.LocalTimeAdapter
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
object NetworkModule {

    private const val HTTP_CLIENT_TIMEOUT_SECONDS = 60L
    private const val CACHE_SIZE = 300L * 1024 * 1024 // 300 MB

    const val DISNEY_FAKE_API_TAG = "DISNEY_FAKE_API_TAG"

    fun create() = DI.Module("NetworkModule") {

        bind<OkHttpClient>() with singleton {
            val context: Context = instance()

            val httpLoggingInterceptor = HttpLoggingInterceptor { message ->
                Timber.tag("OkHttpClient").d(message)
            }.apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }

            OkHttpClient.Builder()
                .connectTimeout(HTTP_CLIENT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(HTTP_CLIENT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(HTTP_CLIENT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .cache(Cache(context.cacheDir, CACHE_SIZE))
                .addInterceptor(httpLoggingInterceptor)
                .build()
        }

        bind<Retrofit>() with multiton { baseUrl: String ->
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(instance())
                .addConverterFactory(MoshiConverterFactory.create(instance()))
                .build()
        }

        bind<Moshi>() with singleton {
            Moshi.Builder()
                .add(LocalDateTimeAdapter())
                .add(LocalTimeAdapter())
                .add(LocalDateAdapter())
                // During conversion, Moshi will try to use the adapters in the order you added them. Only if it fails,
                // it will try the next adapter.KotlinJsonAdapterFactory is the most general one, hence should be called
                // last, after all of your custom and special cases were covered
                .add(KotlinJsonAdapterFactory())
                .build()
        }

        bind<DisneyApi>() with singleton {
            val retrofit: Retrofit = instance(arg = BuildConfig.DISNEY_API_BASE_URL)
            retrofit.create(DisneyApi::class.java)
        }

        bind<DisneyApi>(DISNEY_FAKE_API_TAG) with singleton {
            DisneyFakeApi()
        }

    }
}