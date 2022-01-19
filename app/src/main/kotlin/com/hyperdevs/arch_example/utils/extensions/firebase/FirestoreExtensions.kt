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

import com.google.firebase.firestore.FirebaseFirestore

/**
 * Get the whole device tokens collection.
 */
fun FirebaseFirestore.getDeviceTokens() = this.collection(FirestoreConstants.DeviceToken.COLLECTION)

/**
 * Get a specific document from the device token collection based on the user's UID.
 */
fun FirebaseFirestore.getDeviceToken(uid: String) = this.getDeviceTokens().document(uid)