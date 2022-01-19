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

@file:Suppress("Indentation")

package com.hyperdevs.arch_example.utils.extensions.firebase

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.hyperdevs.arch_example.connectivity.ConnectivityUtils
import com.hyperdevs.arch_example.connectivity.NetworkUnavailableException
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.nio.channels.ClosedChannelException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Coroutine support to Firebase Task interface
 *
 * This method is not intended to be used in your code. You should probably use [await] since it's
 * more idiomatic.
 *
 * The implementation is pretty simple. It just uses a [suspendCoroutine] to encapsulate the
 * Firebase [com.google.android.gms.tasks.OnCompleteListener] interface.
 *
 */
private suspend fun <T> awaitTask(task: Task<T>): T =
    suspendCancellableCoroutine { continuation ->
        task.addOnSuccessListener { continuation.resume(it) }
            .addOnFailureListener { continuation.resumeWithException(it) }
            .addOnCanceledListener { continuation.cancel() }
    }

/**
 * Coroutine support to Firebase Task interface
 *
 * This extension function allows you to interact with a Firebase
 * [com.google.android.gms.tasks.Task] using the `await()` method instead of the standard listeners.
 *
 * There is a sample code below comparing the two approaches. Assuming that
 * `auth` variable has the value returned from `FirebaseAuth.getInstance()`
 * method call then your code can be something like:
 *
 * ```
 * auth.getUserByEmail(email)
 *   .addOnSuccessListener { user -> println(user) }
 *   .addOnFailureListener { exception -> println(exception) }
 * ```
 *
 * When using the coroutine approach, it should be more like this:
 *
 * ```
 * try {
 *   val user = auth.getUserByEmail(email).await()
 *   println(user)
 * } catch (exception: Exception) {
 *   println(exception)
 * }
 * ```
 *
 * @param T The type of the value been returned
 * @throws Exception Thrown in case of network error or other reasons described in the Firebase docs
 * @return The value returned by the Firebase success callback
 */
suspend fun <T> Task<T>.await(): T = awaitTask(this)

/**
 * Wrapper method over [await] to return a typed [Pair] of the Firebase API [Task] together with a
 * result [mini.Task] to reduce boilerplate over controllers methods.
 */
suspend fun <T> Task<T>.awaitForTask(): Pair<T?, mini.Task> {
    val result = runCatching {
        awaitTask(this)
    }
    return result.getOrNull() to
        if (result.isSuccess) mini.Task.success() else mini.Task.failure(result.exceptionOrNull())
}

/**
 * Wrapper method over [await] to return a typed [Pair] of the Firebase API [Task] together with a
 * result [mini.Task] to reduce boilerplate over controllers methods. The difference with [awaitForTask] is that this
 * method should be used for tasks that doesn't manage the offline case such as the calls to callable cloud functions.
 * In those cases if there isn't any internet connection, the generic [FirebaseException] is thrown. Using this method,
 * we send in the resulting task a [NetworkUnavailableException] instead of the generic [FirebaseException].
 */
suspend fun <T> Task<T>.awaitForNotOfflineTask(context: Context): Pair<T?, mini.Task> =
    with(awaitForTask()) {
        if ((!second.isSuccess || first == null) && !ConnectivityUtils.hasInternetConnection(context)) {
            first to mini.Task.failure(NetworkUnavailableException)
        } else {
            this
        }
    }

/**
 * Creates a [Flow] that will emit [SnapshotListenerEvent] event.
 * Flow calls works as cold observables, they are create and only start working after [Flow.collect]
 * is called.
 * [Flow] can't be cancelled, the only way to dispose them is to cancel the [Job] where the
 * [Flow.collect] was called.
 *
 * This specific case is using a [Channel] as a flow to be able to use the [Channel.invokeOnClose]
 * method to remove the listener when this [Flow] is canceled.
 *
 * If [skipFirstCacheHit] is set to true, the values recovered on the first cache hit will be saved until another
 * emission coming from the server is received so we can emit them together.
 */
@SuppressLint("RestrictedApi", "BinaryOperationInTimber")
@Suppress("StringLiteralDuplication")
inline fun <reified FirebaseModel, Model> Query.snapshotChangesFlow(
    skipFirstCacheHit: Boolean,
    crossinline documentMapping: (DocumentSnapshot) -> FirebaseModel? =
        { it.toObjectWithEstimatedTimestamp(FirebaseModel::class.java) },
    crossinline mappingFn: (FirebaseModel, String) -> Model): Flow<SnapshotListenerEvent<Model>> {

    return callbackFlow {
        var firstCacheHitReceived = false
        var cachedDataToSend: SnapshotListenerEvent.FirestoreDocumentChanges<Model>? = null

        val listener =
            this@snapshotChangesFlow.addSnapshotListener(
                MetadataChanges.INCLUDE,
                EventListener<QuerySnapshot?> { querySnapshot, error ->
                    if (error != null) {
                        close(error)
                        return@EventListener // TODO do we want to close and return?
                    }

                    val rawQuery = querySnapshot.getRawQuery()

                    Timber.tag("snapshotChangesFlow")
                        .d("Is data from query ${rawQuery?.path} retrieved from cache?: " +
                            "${querySnapshot?.metadata?.isFromCache}")

                    querySnapshot?.let {
                        //isEmpty represents the documents in the collection, but not the changes made.
                        //We need to check both to avoid skip REMOVED items when there is none left.
                        if (!it.isEmpty || it.documentChanges.isNotEmpty()) {
                            val dataToSend = SnapshotListenerEvent.fromSnapshot(it, mappingFn, documentMapping)

                            if (skipFirstCacheHit && !firstCacheHitReceived && it.metadata.isFromCache) {
                                Timber.tag("snapshotChangesFlow").d("First cache hit for query ${rawQuery?.path}, " +
                                    "cached data: ${dataToSend.changesSummary()}")

                                firstCacheHitReceived = true
                                cachedDataToSend = dataToSend
                            } else {
                                var newDataToSend = dataToSend
                                if (cachedDataToSend != null) {
                                    // Combine cached data with new changes if available
                                    newDataToSend = SnapshotListenerEvent.FirestoreDocumentChanges(
                                        cachedDataToSend!!.changes.plus(dataToSend.changes))

                                    cachedDataToSend = null
                                }
                                Timber.tag("snapshotChangesFlow").d("Returning value for query ${rawQuery?.path}: " +
                                    "${newDataToSend.changesSummary()}")
                                channel.trySend(newDataToSend)
                            }
                        } else {
                            channel.trySend(SnapshotListenerEvent.IsEmpty<Model>())
                        }

                        if (!it.metadata.hasPendingWrites()) {
                            channel.trySend(SnapshotListenerEvent.HasNotPendingWrites<Model>())
                        }
                    }
                })
        awaitClose { listener.remove() }
    }
}

/**
 * Returns the raw [com.google.firebase.firestore.core.Query] object from a [QuerySnapshot].
 *
 * ONLY USED FOR DEBUG PURPOSES. If you use the function outside of debug environments, you'll receive null as a result.
 */
fun QuerySnapshot?.getRawQuery(): com.google.firebase.firestore.core.Query? {
    try {
        if (BuildConfig.DEBUG) {
            return Query::class.java.getDeclaredField("query").let {
                it.isAccessible = true
                return@let it.get(this?.query) as? com.google.firebase.firestore.core.Query
            }
        } else {
            return null
        }
    } catch (e: Exception) {
        return null
    }
}

/**
 * Returns a map representing the quantity of changes that a given [SnapshotListenerEvent.FirestoreDocumentChanges]
 * contains.
 *
 * Used for debug purposes.
 */
fun <Model> SnapshotListenerEvent.FirestoreDocumentChanges<Model>.changesSummary() =
    this.changes.groupBy { it.type }.mapValues { (_, value) -> value.size }

/**
 * Creates a [Flow] to filter [SnapshotListenerEvent.FirestoreDocumentChanges]
 * coming from [SnapshotListenerEvent] events to produce a list of [Model] typed values.
 * Flow calls works as cold observables, they are create and only start working after
 * [Flow.collect] is called.
 *
 * [Flow] can't be cancelled, the only way to dispose them is to cancel the [Job] where the
 * [Flow.collect] was called.
 *
 * This specific case is using a [Channel] as a flow to be able to use the [Channel.invokeOnClose]
 * method to remove the listener when this [Flow] is canceled.
 *
 * If [skipFirstCacheHit] is set to true, the values recovered on the first cache hit will be saved until another
 * emission coming from the server is received so we can emit them together.
 */
inline fun <reified FirebaseModel, Model> Query.snapshotFlow(
    skipFirstCacheHit: Boolean,
    crossinline mappingFn: (FirebaseModel, String) -> Model): Flow<List<Model>> {
    val modelFlow: Flow<SnapshotListenerEvent.FirestoreDocumentChanges<Model>> =
        snapshotChangesFlow(
            skipFirstCacheHit = skipFirstCacheHit,
            mappingFn = mappingFn).filterIsInstance()
    return modelFlow.map { it.changes.map { change -> change.document.model } }
}

/**
 * Creates a [Flow] that will emit [SnapshotListenerEvent] events.
 * Flow calls work as cold observables, they are created and only start working after [Flow.collect]
 * is called.
 *
 * [Flow] can't be cancelled , the only way to dispose them is to cancel the [Job] where the
 * [Flow.collect] was called.
 *
 * This specific case is using a [Channel] as a flow to be able to use the [Channel.invokeOnClose]
 * method to remove the listener when this [Flow] is canceled.
 */
inline fun <reified FirebaseModel, Model> DocumentReference.snapshotFlow(
    crossinline documentMapping: (DocumentSnapshot) -> FirebaseModel? =
        { it.toObjectWithEstimatedTimestamp(FirebaseModel::class.java) },
    crossinline mappingFn: (FirebaseModel, String) -> Model,
    includeMetadata: Boolean = true): Flow<SnapshotListenerEvent<Model>> =
    callbackFlow {
        val metadataChanges =
            if (includeMetadata) MetadataChanges.INCLUDE else MetadataChanges.EXCLUDE
        val listener =
            this@snapshotFlow.addSnapshotListener(metadataChanges,
                EventListener<DocumentSnapshot?> { documentSnapshot, error ->

                    if (error != null) {
                        channel.close(error)
                        return@EventListener // TODO do we want to close and return?
                    }

                    documentSnapshot?.let {
                        if (it.exists()) {
                            channel.trySend(SnapshotListenerEvent.fromSnapshot(it, mappingFn, documentMapping))
                        } else {
                            channel.trySend(SnapshotListenerEvent.IsEmpty<Model>())
                        }

                        if (includeMetadata && !it.metadata.hasPendingWrites()) {
                            channel.trySend(SnapshotListenerEvent.HasNotPendingWrites<Model>())
                        }

                    }
                })
        awaitClose { listener.remove() }
    }

/**
 * Creates a [Channel] that tries send all the changes inside the current [DocumentReference] and map
 * it to the provided field given by [fieldName]. The [ListenerRegistration] will be removed when
 * the channel closes or an error happens.
 *
 * This method should not be used outside of [onFieldUpdated] and [onFieldUpdatedOrNull].
 * For cases where we would like to receive all the changes of a specific field we should use
 * [snapshotFlow] with a mapping clause.
 */
inline fun <reified T> DocumentReference.documentFieldChannel(fieldName: String): ReceiveChannel<T?> {
    val channel = Channel<T?>()
    val listener: ListenerRegistration = this.addSnapshotListener(
        EventListener<DocumentSnapshot?> { snapshot, error ->
            error?.let { channel.close(error) }

            val value = snapshot?.get(fieldName, T::class.java)
            channel.trySend(value)
        })
    channel.invokeOnClose { listener.remove() }
    return channel
}

/**
 * Creates a [CompletableDeferred] that will wait until the given [fieldName] changes to a new
 * nullable value.
 *
 * If an error happens while waiting for the result, the [CompletableDeferred] will complete
 * with a null value.
 */
suspend inline fun <reified T> DocumentReference.onFieldUpdatedOrNull(
    fieldName: String,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO): CompletableDeferred<T?> {

    val deferred = CompletableDeferred<T?>()
    val docChannel: ReceiveChannel<T?> = documentFieldChannel(fieldName)
    deferred.invokeOnCompletion { docChannel.cancel() }

    withContext(coroutineDispatcher) {
        while (!docChannel.isClosedForReceive) {
            val value = docChannel.receive()
            if (docChannel.isClosedForReceive) {
                deferred.complete(null)
            } else {
                deferred.complete(value)
            }
        }
    }
    return deferred
}

/**
 * Creates a [CompletableDeferred] with a timeout that will wait until the given [fieldName]
 * changes to a new nullable value or the timeout expires.
 *
 * If an error happens while waiting for the result, the [CompletableDeferred] will complete
 * exceptionally with the given error.
 *
 * If the timeout expires, a [TimeoutCancellationException] will be throw.
 */
suspend inline fun <reified T> DocumentReference.onFieldUpdatedOrNull(fieldName: String,
                                                                      timeOutMillis: Long): T? =
    withTimeout(timeOutMillis) { return@withTimeout onFieldUpdatedOrNull<T>(fieldName).await() }

/**
 * Creates a [CompletableDeferred] that will wait until the given [fieldName] changes to a new
 * non nullable value.
 *
 * If an error happens while waiting for the result, the [CompletableDeferred] will complete
 * exceptionally with the given error.
 */
suspend inline fun <reified T> DocumentReference.onFieldUpdated(
    fieldName: String,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO): CompletableDeferred<T> {

    val deferred = CompletableDeferred<T>()
    val docChannel = documentFieldChannel<T?>(fieldName)
    deferred.invokeOnCompletion { docChannel.cancel() }

    withContext(coroutineDispatcher) {
        while (!docChannel.isClosedForReceive) {
            val value = docChannel.receive()
            if (docChannel.isClosedForReceive) {
                deferred.completeExceptionally(ClosedChannelException())
            } else {
                value?.let { deferred.complete(it) }
            }
        }
    }

    return deferred
}

/**
 * Creates a [CompletableDeferred] with a timeout that will wait until the given [fieldName]
 * changes to a new non nullable value or the timeout expires.
 *
 * If an error happens while waiting for the result, the [CompletableDeferred] will complete
 * exceptionally with the given error.
 *
 * If the timeout expires, a [TimeoutCancellationException] will be throw.
 */
suspend inline fun <reified T> DocumentReference.onFieldUpdated(fieldName: String,
                                                                timeOutMillis: Long): T =
    withTimeout(timeOutMillis) { return@withTimeout onFieldUpdated<T>(fieldName).await() }