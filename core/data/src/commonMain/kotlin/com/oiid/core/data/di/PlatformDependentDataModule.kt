package com.oiid.core.data.di

import com.oiid.core.data.utils.NetworkMonitor
import org.koin.core.module.Module

interface PlatformDependentDataModule {
    val networkMonitor: NetworkMonitor
}

expect val platformModule: Module

expect val getPlatformDataModule: PlatformDependentDataModule
