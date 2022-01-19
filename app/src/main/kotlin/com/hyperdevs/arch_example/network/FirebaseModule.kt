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

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.messaging.FirebaseMessaging
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
object FirebaseModule {

    private const val MINIMUM_FETCH_INTERVAL_SECONDS = 60L

    fun create() = DI.Module("FirebaseModule") {

        bind<FirebaseAuth>() with singleton {
            FirebaseAuth.getInstance()
        }

        bind<FirebaseFirestore>() with singleton {
            val settings = FirebaseFirestoreSettings.Builder()
                // Enable cache for offline mode
                .setPersistenceEnabled(true)
                .build()
            FirebaseFirestore.getInstance().apply { firestoreSettings = settings }
        }

        bind<FirebaseMessaging>() with singleton {
            FirebaseMessaging.getInstance()
        }

        bind<FirebaseCrashlytics>() with singleton {
            FirebaseCrashlytics.getInstance()
        }

//        bind<FirebaseRemoteConfig>() with singleton {
//            val configSettings = FirebaseRemoteConfigSettings.Builder()
//                .setMinimumFetchIntervalInSeconds(MINIMUM_FETCH_INTERVAL_SECONDS)
//                .build()
//            FirebaseRemoteConfig.getInstance().apply {
//                setConfigSettingsAsync(configSettings)
//                setDefaultsAsync(R.xml.remote_config_defaults)
//            }
//        }
    }
}