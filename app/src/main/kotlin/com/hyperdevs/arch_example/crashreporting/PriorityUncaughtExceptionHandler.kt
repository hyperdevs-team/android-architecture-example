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

package com.hyperdevs.arch_example.crashreporting

/**
 * Class that allows setting uncaught exception handlers with a given priority of execution.
 *
 * Use the constants declared in this class to give a priority when needed.
 */
data class PriorityUncaughtExceptionHandler(
    val id: String,
    val priority: Int = DEFAULT_PRIORITY,
    private val onUncaught: (Thread, Throwable) -> Unit) :
    Thread.UncaughtExceptionHandler, Comparable<PriorityUncaughtExceptionHandler> {

    override fun uncaughtException(t: Thread, e: Throwable) {
        onUncaught(t, e)
    }

    companion object {
        const val VERY_HIGH_PRIORITY = 5
        const val HIGH_PRIORITY = 25
        const val DEFAULT_PRIORITY = 50
        const val LOW_PRIORITY = 75
        const val VERY_LOW_PRIORITY = 100
    }

    override fun compareTo(other: PriorityUncaughtExceptionHandler): Int {
        val comparison = this.priority.compareTo(other.priority)
        return if (comparison == 0) this.id.compareTo(other.id) else comparison
    }
}

/**
 * Converts a standard [Thread.UncaughtExceptionHandler] to a priority one.
 */
fun Thread.UncaughtExceptionHandler.toPriorityUncaughtExceptionHandler(
    id: String,
    priority: Int = PriorityUncaughtExceptionHandler.DEFAULT_PRIORITY) =

    PriorityUncaughtExceptionHandler(id, priority) { thread, throwable ->
        this.uncaughtException(thread, throwable)
    }