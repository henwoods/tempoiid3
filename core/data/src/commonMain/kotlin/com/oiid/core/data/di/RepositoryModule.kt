package com.oiid.core.data.di

import com.oiid.core.data.profile.ProfileService
import com.oiid.core.data.profile.impl.ProfileServiceImpl
import com.oiid.core.data.user.UserRepository
import com.oiid.core.data.user.impl.UserRepositoryImpl
import com.oiid.core.data.utils.NetworkMonitor
import org.koin.dsl.module

val DataModule = module {
    includes(platformModule)

    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<ProfileService> { ProfileServiceImpl(get()) }
    single<PlatformDependentDataModule> { getPlatformDataModule }
    single<NetworkMonitor> { getPlatformDataModule.networkMonitor }
}
