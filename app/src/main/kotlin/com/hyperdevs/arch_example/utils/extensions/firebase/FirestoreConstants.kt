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

package com.hyperdevs.arch_example.utils.extensions.firebase

/**
 * Constants used to identify the Firestore data model.
 */
object FirestoreConstants {

    /**
     * DeviceToken collection along with its document fields.
     */
    object DeviceToken {
        const val COLLECTION = "deviceTokens"
        const val MOBILE_DEVICE_TOKEN_FIELD = "mobileDeviceToken"
        const val WEB_DEVICE_TOKEN_FIELD = "webDeviceToken"
    }

}