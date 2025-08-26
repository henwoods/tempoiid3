package com.oiid.network.api

import com.oiid.core.model.api.UserInfoNetwork
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header

interface UserApiService {
    @GET("app/users/userinfo")
    suspend fun getUserInfo(@Header("x-session-id") sessionId: String): UserInfoNetwork
}
