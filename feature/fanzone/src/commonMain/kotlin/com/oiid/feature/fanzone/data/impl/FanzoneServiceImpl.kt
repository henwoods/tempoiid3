package com.oiid.feature.fanzone.data.impl

import com.oiid.core.datastore.FeedServiceImpl as BaseFeedServiceImpl
import com.oiid.core.config.artistId
import com.oiid.core.datastore.UserPreferencesRepository
import com.oiid.network.api.FanzoneApiService

/**
 * Wrapper for DI
 */
class FanzoneServiceImpl(fanzoneApiService: FanzoneApiService, userPreferencesRepository: UserPreferencesRepository) :
    BaseFeedServiceImpl(
        config = FanzoneFeedServiceAdapter(fanzoneApiService),
        userPreferencesRepository = userPreferencesRepository,
        artistId = artistId(),
    )
