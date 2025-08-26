package oiid.core.base.datastore.reactive

import kotlinx.coroutines.flow.Flow

/**
 * Interface for observing value changes in the data store as flows.
 *
 * Implementations of this interface provide mechanisms to create flows
 * that emit values for specific keys, supporting both initial and distinct emissions.
 *
 * Example usage:
 * ```kotlin
 * val observer: ValueObserver = DefaultValueObserver(changeNotifier)
 * observer.createValueFlow("theme", "light") { getTheme() }
 * ```
 */
interface ValueObserver {
    /**
     * Creates a flow that emits the value for the specified key.
     *
     * @param key The preference key to observe.
     * @param default The default value to emit if retrieval fails.
     * @param getter A suspend function to retrieve the value.
     * @return A [Flow] emitting the value for the key.
     */
    fun <T> createValueFlow(
        key: String,
        default: T,
        getter: suspend () -> Result<T>,
    ): Flow<T>

    /**
     * Creates a flow that emits only distinct values for the specified key, suppressing duplicates.
     *
     * @param key The preference key to observe.
     * @param default The default value to emit if retrieval fails.
     * @param getter A suspend function to retrieve the value.
     * @return A [Flow] emitting only distinct values for the key.
     */
    fun <T> createDistinctValueFlow(
        key: String,
        default: T,
        getter: suspend () -> Result<T>,
    ): Flow<T>
}
