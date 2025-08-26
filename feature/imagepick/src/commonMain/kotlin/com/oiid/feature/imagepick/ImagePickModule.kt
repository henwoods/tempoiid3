package com.oiid.feature.imagepick

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val ImagePickModule = module {
    viewModelOf(::ImagePickViewModel)
}
