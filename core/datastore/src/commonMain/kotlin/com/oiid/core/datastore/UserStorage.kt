package com.oiid.core.datastore

import com.oiid.core.model.UserInfo
import com.russhwolf.settings.Settings

interface UserStorage {
    suspend fun saveUserInfo(userInfo: UserInfo)
    suspend fun getUserId(): String?
    suspend fun getUserInfo(): UserInfo?
    suspend fun clearUserInfo()
}

class SettingsUserStorage(
    private val settings: Settings,
) : UserStorage {
    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_ENCRYPTION_KEY = "encryption_key"
        private const val KEY_REGION = "region"
        private const val KEY_SUBSCRIPTION_ACTIVE = "subscription_active"
        private const val KEY_CREATED_AT = "created_at"
        private const val KEY_UPDATED_AT = "updated_at"
        private const val KEY_ARTIST_ID_POST_PERMISSION = "artist_id_post_permission"
    }

    override suspend fun saveUserInfo(userInfo: UserInfo) {
        settings.putString(KEY_USER_ID, userInfo.userId)

        userInfo.encryptionKey?.let {
            settings.putString(KEY_ENCRYPTION_KEY, it)
        }

        userInfo.region?.let {
            settings.putString(KEY_REGION, it)
        }

        userInfo.subscription?.let {
            settings.putBoolean(KEY_SUBSCRIPTION_ACTIVE, it.active)
        }

        userInfo.createdAt?.let {
            settings.putString(KEY_CREATED_AT, it)
        }

        userInfo.updatedAt?.let {
            settings.putString(KEY_UPDATED_AT, it)
        }

        userInfo.artistIdPostPermission?.let {
            settings.putString(KEY_ARTIST_ID_POST_PERMISSION, it)
        }
    }

    override suspend fun getUserId(): String? = settings.getStringOrNull(KEY_USER_ID)

    override suspend fun getUserInfo(): UserInfo? {
        val userId = getUserId() ?: return null

        return UserInfo(
            userId = userId,
            encryptionKey = settings.getStringOrNull(KEY_ENCRYPTION_KEY),
            region = settings.getStringOrNull(KEY_REGION),
            subscription = settings.getStringOrNull(KEY_SUBSCRIPTION_ACTIVE)?.let {
                com.oiid.core.model.Subscription(active = it.toBoolean())
            },
            createdAt = settings.getStringOrNull(KEY_CREATED_AT),
            updatedAt = settings.getStringOrNull(KEY_UPDATED_AT),
            artistIdPostPermission = settings.getStringOrNull(KEY_ARTIST_ID_POST_PERMISSION),
        )
    }

    override suspend fun clearUserInfo() {
        settings.remove(KEY_USER_ID)
        settings.remove(KEY_ENCRYPTION_KEY)
        settings.remove(KEY_REGION)
        settings.remove(KEY_SUBSCRIPTION_ACTIVE)
        settings.remove(KEY_CREATED_AT)
        settings.remove(KEY_UPDATED_AT)
        settings.remove(KEY_ARTIST_ID_POST_PERMISSION)
    }
}
