package oiid.core.base.datastore.di

import com.oiid.core.common.di.AppDispatchers
import com.russhwolf.settings.Settings
import oiid.core.base.datastore.contracts.ReactiveDataStore
import oiid.core.base.datastore.factory.DataStoreFactory
import oiid.core.base.datastore.reactive.PreferenceFlowOperators
import oiid.core.base.datastore.repository.ReactivePreferencesRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Koin module for providing core datastore dependencies.
 *
 * Usage Example:
 * ```kotlin
 * startKoin {
 *     modules(CoreDatastoreModule)
 * }
 * ```
 */
val CoreDatastoreModule = module {

    // Platform-specific Settings instance
    single<Settings> { Settings() }

    // Main reactive datastore repository (recommended for most use cases)
    single<ReactivePreferencesRepository> {
        DataStoreFactory()
            .settings(get())
            .dispatcher(get(named(AppDispatchers.IO.name)))
            .cacheSize(200)
            .build()
    }

    // Direct access to reactive datastore (if needed for specific use cases)
    single<ReactiveDataStore> {
        DataStoreFactory()
            .settings(get())
            .dispatcher(get(named(AppDispatchers.IO.name)))
            .cacheSize(200)
            .buildDataStore()
    }

    // Flow operators for advanced reactive operations
    single<PreferenceFlowOperators> {
        PreferenceFlowOperators(get<ReactivePreferencesRepository>())
    }
}
