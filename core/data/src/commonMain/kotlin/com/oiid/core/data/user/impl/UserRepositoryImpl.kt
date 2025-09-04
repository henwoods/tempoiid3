package com.oiid.core.data.user.impl

import co.touchlab.kermit.Logger
import com.oiid.core.data.user.UserRepository
import com.oiid.core.datastore.UserStorage
import com.oiid.core.model.UserInfo
import com.oiid.core.model.api.Resource
import com.oiid.network.api.UserApiService
import kotlin.random.Random

class UserRepositoryImpl(
    private val userApiService: UserApiService,
    private val userStorage: UserStorage,
) : UserRepository {

    private var sessionId = generateSessionId()

    private fun generateSessionId(): String {
        val allowedChars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..36).map { allowedChars.random(Random.Default) }.joinToString("")
    }

    override suspend fun fetchAndStoreUserInfo(): Resource<UserInfo> {
        return try {
            if (sessionId.isBlank()) {
                sessionId = generateSessionId()
            }
            val userInfoNetwork = userApiService.getUserInfo(sessionId)
            val userInfo = userInfoNetwork.toUserInfo()
            userStorage.saveUserInfo(userInfo)
            Logger.Companion.d("User info fetched and stored successfully: ${userInfo.userId}")
            Resource.Success(userInfo, false)
        } catch (e: Exception) {
            Logger.Companion.e("Failed to fetch user info", e)
            Resource.Error(e)
        }
    }

    override suspend fun getUserId(): String? {
        return userStorage.getUserId()
    }

    override suspend fun getUserInfo(): UserInfo? {
        return userStorage.getUserInfo()
    }

    override suspend fun clearUserInfo() {
        userStorage.clearUserInfo()
        sessionId = ""
    }
}
