package com.oiid.feature.fanzone.data.impl

import com.oiid.core.config.artistId
import com.oiid.core.datastore.PostServiceAdapter
import com.oiid.core.datastore.PostServiceImpl
import com.oiid.core.datastore.UserPreferencesRepository

/**
 * Wrapper for DI
 */
class FanzonePostServiceImpl(
    adapter: PostServiceAdapter, userPreferencesRepository: UserPreferencesRepository, postId: String
) : PostServiceImpl(adapter, userPreferencesRepository, postId, artistId())