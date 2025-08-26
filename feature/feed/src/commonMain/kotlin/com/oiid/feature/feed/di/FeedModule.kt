package com.oiid.feature.feed.di

import com.oiid.core.config.artistId
import com.oiid.feature.feed.data.impl.FeedPostServiceAdapter
import com.oiid.feature.feed.data.impl.FeedPostServiceImpl
import com.oiid.feature.feed.data.impl.FeedServiceImpl
import com.oiid.feature.feed.detail.FeedDetailViewModel
import com.oiid.feature.feed.list.FeedListViewModel
import com.oiid.feature.feed.list.FeedViewModel
import com.oiid.network.api.FeedApiService
import com.oiid.network.api.PostApiService
import com.oiid.network.api.createFeedApiService
import com.oiid.network.api.createPostApiService
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val FeedModule = module {
    single<FeedApiService> {
        get<Ktorfit>().createFeedApiService()
    }

    single<PostApiService> {
        get<Ktorfit>().createPostApiService()
    }

    single<FeedServiceImpl> { FeedServiceImpl(get(), get(), get()) }

    factory<FeedPostServiceImpl> { (postId: String) ->
        FeedPostServiceImpl(
            config = FeedPostServiceAdapter(get(FeedServiceImpl::class), get()),
            userPreferencesRepository = get(),
            postId = postId,
        )
    }

    viewModel {
        FeedViewModel(feedService = get(), userPreferencesRepository = get())
    }

    viewModel {
        FeedListViewModel()
    }

    viewModel { (postId: String) ->
        FeedDetailViewModel(
            postId = postId, artistId = artistId(),
            postService = get<FeedPostServiceImpl>(clazz = FeedPostServiceImpl::class) {
                parametersOf(postId)
            },
        )
    }
}
