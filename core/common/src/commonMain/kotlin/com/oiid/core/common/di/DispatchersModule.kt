package com.oiid.core.common.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val DispatchersModule = module {
    includes(ioDispatcherModule)
    single<CoroutineDispatcher>(named(AppDispatchers.Default.name)) { Dispatchers.Default }
    single<CoroutineDispatcher>(named(AppDispatchers.Unconfined.name)) { Dispatchers.Unconfined }
    single<CoroutineScope>(named("ApplicationScope")) {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}

/**
 * This module provides the default dispatchers for the application.
 * The default dispatcher is used for all coroutines that are launched in the application.
 * The IO dispatcher is used for all coroutines that perform IO operations.
 * The Unconfined dispatcher is used for all coroutines that are not bound to any specific thread.
 * The ApplicationScope is used to launch coroutines that are bound to the lifecycle of the application.
 *
 */
enum class AppDispatchers {
    Default,
    IO,
    Unconfined,
}

expect val ioDispatcherModule: Module
