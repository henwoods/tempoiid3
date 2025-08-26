package com.oiid.feature.auth

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val OnboardingModule = module {
    viewModelOf(::OnboardingViewmodel)
}
