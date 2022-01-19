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

import com.google.firebase.firestore.DocumentSnapshot

/**
 * Does the same as [DocumentSnapshot.toObject], but sets up the server timestamp behaviour so whenever we set
 * a null timestamp in a field marked with the [com.google.firebase.firestore.ServerTimestamp] annotation,
 * Firebase cache responses return an estimated timestamp (usually the device's clock timestamp) until
 * the proper timestamp is written in the server and returned to the client.
 *
 * This is required so we don't receive null values in Firebase responses in these fields.
 *
 * Docs: https://firebase.google.com/docs/reference/android/com/google/firebase/firestore/DocumentSnapshot.ServerTimestampBehavior
 */
fun <T> DocumentSnapshot.toObjectWithEstimatedTimestamp(clazz: Class<T>) =
    this.toObject(clazz, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)