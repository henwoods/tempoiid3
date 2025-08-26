package com.oiid.feature.auth

import com.oiid.core.data.auth.AuthRepository
import com.oiid.core.data.auth.impl.AuthRepositoryImpl
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val AuthModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get(), get()) }

    viewModelOf(::AuthViewModel)
}
