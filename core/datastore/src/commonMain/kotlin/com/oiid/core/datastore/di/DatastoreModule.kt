package com.oiid.core.datastore.di

import com.oiid.core.datastore.SettingsUserStorage
import com.oiid.core.datastore.UserPreferencesRepository
import com.oiid.core.datastore.UserPreferencesRepositoryImpl
import com.oiid.core.datastore.UserStorage
import com.russhwolf.settings.Settings
import oiid.core.base.datastore.di.CoreDatastoreModule
import oiid.core.base.datastore.factory.DataStoreFactory
import org.koin.dsl.module

val DatastoreModule = module {
    includes(CoreDatastoreModule)

    single<UserPreferencesRepository> {
        UserPreferencesRepositoryImpl(preferencesStore = DataStoreFactory.create())
    }

    single<UserStorage> {
        SettingsUserStorage(settings = get<Settings>())
    }
}
