package oiid.core.base.datastore.notification

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import oiid.core.base.datastore.contracts.DataStoreChangeEvent
import oiid.core.base.datastore.reactive.DefaultChangeNotifier
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultChangeNotifierTest {

    private val changeNotifier = DefaultChangeNotifier()

    @Test
    fun observeChanges_EmitsNotifiedChanges() = runTest {
        changeNotifier.observeChanges().test {
            val change = DataStoreChangeEvent.ValueAdded("key", "value")
            changeNotifier.notifyChange(change)

            assertEquals(change, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun observeKeyChanges_FiltersCorrectKey() = runTest {
        changeNotifier.observeKeyChanges("target_key").test {
            // Send change for different key - should not emit
            changeNotifier.notifyChange(DataStoreChangeEvent.ValueAdded("other_key", "value"))

            // Send change for target key - should emit
            val targetChange = DataStoreChangeEvent.ValueAdded("target_key", "value")
            changeNotifier.notifyChange(targetChange)
            assertEquals(targetChange, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun observeKeyChanges_EmitsGlobalChanges() = runTest {
        changeNotifier.observeKeyChanges("specific_key").test {
            // Global change (clear all) should be emitted regardless of key
            val globalChange = DataStoreChangeEvent.StoreCleared()
            changeNotifier.notifyChange(globalChange)
            assertEquals(globalChange, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }
}
