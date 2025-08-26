package com.oiid.feature.feed.data.impl

import com.oiid.core.datastore.FeedServiceImpl as BaseFeedServiceImpl
import com.oiid.core.config.artistId
import com.oiid.core.datastore.UserPreferencesRepository
import com.oiid.network.api.FeedApiService
import com.oiid.network.api.PostApiService

private const val FEED_ITEMS_KEY = "ALL_FEED_ITEMS"

/**
 * Wrapper for DI
 */
class FeedServiceImpl(
    feedApiService: FeedApiService,
    postApiService: PostApiService,
    userPreferencesRepository: UserPreferencesRepository,
) : BaseFeedServiceImpl(
    config = FeedItemsServiceAdapter(feedApiService, postApiService),
    userPreferencesRepository = userPreferencesRepository,
    artistId = artistId(),
)
