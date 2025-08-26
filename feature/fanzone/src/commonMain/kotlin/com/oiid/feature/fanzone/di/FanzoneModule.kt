package com.oiid.feature.fanzone.di

import com.oiid.core.config.artistId
import com.oiid.core.designsystem.composable.ScrollStateViewModel
import com.oiid.feature.fanzone.data.impl.FanzonePostServiceAdapter
import com.oiid.feature.fanzone.data.impl.FanzonePostServiceImpl
import com.oiid.feature.fanzone.data.impl.FanzoneServiceImpl
import com.oiid.feature.fanzone.detail.FanzonePostPostDetailViewModel
import com.oiid.feature.fanzone.list.FanzoneFeedViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val FanzoneModule = module {
    single<FanzoneServiceImpl> {
        FanzoneServiceImpl(
            fanzoneApiService = get(),
            userPreferencesRepository = get(),
        )
    }

    single<FanzonePostServiceAdapter> {
        FanzonePostServiceAdapter(
            fanzoneService = get(clazz = FanzoneServiceImpl::class),
            fanzoneApiService = get(),
        )
    }
    viewModel {
        ScrollStateViewModel()
    }

    viewModel {
        FanzoneFeedViewModel(
            fanzoneService = get(),
        )
    }

    viewModel { (postId: String) ->
        FanzonePostPostDetailViewModel(
            postId = postId,
            artistId = artistId(),
            postService = FanzonePostServiceImpl(
                adapter = get<FanzonePostServiceAdapter>(),
                userPreferencesRepository = get(),
                postId = postId,
            ),
        )
    }
}
