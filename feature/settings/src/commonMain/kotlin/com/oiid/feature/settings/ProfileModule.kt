package com.oiid.feature.settings

import com.oiid.network.api.ProfileApiService
import com.oiid.network.api.createProfileApiService
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val ProfileModule = module {
    single<ProfileApiService> { get<Ktorfit>().createProfileApiService() }

    viewModelOf(::ProfileViewModel)
}
