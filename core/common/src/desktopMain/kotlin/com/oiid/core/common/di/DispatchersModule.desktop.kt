package com.oiid.core.common.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val ioDispatcherModule: Module
    get() = module {
        single<CoroutineDispatcher>(named(AppDispatchers.IO.name)) { Dispatchers.IO }
    }
