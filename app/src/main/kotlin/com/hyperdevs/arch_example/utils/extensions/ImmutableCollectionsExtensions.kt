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

/**
 * Utility methods to work with immutable collections.
 */

/**
 * Replace or append the first item that matches the predicate.
 */
fun <T> List<T>.replaceOrAppend(replacement: T, predicate: (T) -> Boolean): List<T> {
    val idx = this.indexOfFirst(predicate)
    return if (idx == -1) {
        this + replacement
    } else {
        val mutable = this.toMutableList()
        mutable[idx] = replacement
        mutable
    }
}

/**
 * Replace the item in the given position.
 */
fun <T> List<T>.replaceAt(replacement: T, position: Int): List<T> {
    val mutable = this.toMutableList()
    mutable[position] = replacement
    return mutable
}

/**
 * Mutate the value of the first item that matches the predicate.
 */
fun <T> List<T>.mutateFirst(predicate: (T) -> Boolean, mutation: (T) -> (T)): List<T> {
    val idx = indexOfFirst(predicate)
    return if (idx == -1) {
        this
    } else {
        val mutable = this.toMutableList()
        mutable[idx] = mutation(mutable[idx])
        mutable
    }
}

/**
 * Mutate the value corresponding to a given key in a map.
 */
fun <K, V> Map<K, V>.mutateValue(key: K, mutation: (V) -> V): Map<K, V> {
    val mutable = this.toMutableMap()
    if (mutable.containsKey(key)) {
        mutable[key] = mutation(mutable[key]!!)
    }
    return mutable
}

/**
 * Remove all items that match the filter.
 */
fun <T> List<T>.remove(filter: (T) -> Boolean): List<T> {
    val newList = this.toMutableList()
    newList.removeAll { filter(it) }
    return newList
}

/**
 * Remove the item in the given position.
 */
fun <T> List<T>.removeAt(position: Int): List<T> {
    val mutable = this.toMutableList()
    mutable.removeAt(position)
    return mutable
}

/**
 * Add the pair to the map keeping iteration order with the new element being the head.
 */
infix fun <K, V> Map<K, V>.putFirst(pair: Pair<K, V>): Map<K, V> = putFirst(pair.first, pair.second)

/**
 * Add the item to the map keeping iteration order with the new element being the head.
 */
fun <K, V> Map<K, V>.putFirst(key: K, value: V): Map<K, V> =
// Remove the value from the current map, add it back as the first element
    mapOf(key to value) + this.minus(key)

/**
 * Map a not null filtered [Iterable].
 */
fun <T : Any, R> Iterable<T?>.filterNotNullMap(transform: (T) -> R): List<R> = filterNotNull().map(transform)

/**
 * Returns the first element that is instance of specified type parameter R, or `null` if element was not found.
 */
inline fun <reified R> Iterable<*>.firstIsInstanceOrNull(): R? =
    firstOrNull { it is R } as R?

/**
 * Returns a list of all elements sorted according to natural sort order of the String returned by specified [selector]
 * function but being case insensitive.
 */
inline fun <T> Iterable<T>.sortedIgnoreCaseBy(crossinline selector: (T) -> String): List<T> =
    sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, selector))