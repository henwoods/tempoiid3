package cmp.shared.utils

import cmp.navigation.di.KoinModules
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.koinApplication

fun koinConfiguration() = koinApplication {
    modules(KoinModules.allModules)
}

fun initKoin(modules: List<Module> = emptyList(), config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(KoinModules.allModules + modules)
    }
}
