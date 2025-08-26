package com.oiid.feature.fanzone.detail

import com.oiid.core.data.profile.ProfileService
import com.oiid.core.datastore.PostService
import com.oiid.feature.feed.detail.FeedPostDetailViewModel

class FanzonePostPostDetailViewModel(
    postId: String,
    artistId: String,
    postService: PostService,
    profileService: ProfileService,
) :
    FeedPostDetailViewModel(
        postId = postId,
        artistId = artistId,
        postService = postService,
        profileService = profileService,
        isForum = true,
    )