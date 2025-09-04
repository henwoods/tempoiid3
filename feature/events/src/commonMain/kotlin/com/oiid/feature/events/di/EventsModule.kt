package com.oiid.feature.events.di

import com.oiid.feature.events.EventsListViewModel
import com.oiid.feature.events.EventsViewModel
import com.oiid.feature.events.data.EventsService
import com.oiid.feature.events.data.impl.EventsServiceImpl
import com.oiid.network.api.EventsApiService
import com.oiid.network.api.createEventsApiService
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val EventsModule = module {
    single<EventsApiService> {
        get<Ktorfit>().createEventsApiService()
    }

    single<EventsService> {
        EventsServiceImpl(get())
    }

    viewModel {
        EventsViewModel(get())
    }

    viewModel {
        EventsListViewModel()
    }
}
