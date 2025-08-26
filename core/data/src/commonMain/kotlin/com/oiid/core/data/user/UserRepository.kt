package com.oiid.core.data.user

import com.oiid.core.model.UserInfo
import com.oiid.core.model.api.Resource

interface UserRepository {
    suspend fun fetchAndStoreUserInfo(): Resource<UserInfo>

    suspend fun getUserId(): String?

    suspend fun getUserInfo(): UserInfo?

    suspend fun clearUserInfo()
}
