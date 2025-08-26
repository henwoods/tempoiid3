package com.oiid.feature.feed.data.impl

import com.oiid.core.config.artistId
import com.oiid.core.datastore.PostServiceImpl
import com.oiid.core.datastore.UserPreferencesRepository

/**
 * Wrapper for DI
 */
class FeedPostServiceImpl(
    config: FeedPostServiceAdapter, userPreferencesRepository: UserPreferencesRepository, postId: String,
) : PostServiceImpl(config, userPreferencesRepository, postId, artistId())
