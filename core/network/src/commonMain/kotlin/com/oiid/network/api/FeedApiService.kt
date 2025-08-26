package com.oiid.network.api

import com.oiid.core.model.PostItem
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path

interface FeedApiService {
    @GET("app/artists/{artistId}/feed")
    suspend fun getPosts(@Path("artistId") artistId: String): List<PostItem>
}
