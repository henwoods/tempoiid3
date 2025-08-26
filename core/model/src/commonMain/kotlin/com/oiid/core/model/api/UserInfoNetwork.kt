package com.oiid.core.model.api

import com.oiid.core.model.Subscription
import com.oiid.core.model.UserInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoNetwork(
    @SerialName("userId")
    val userId: String,

    @SerialName("encryptionKey")
    val encryptionKey: String? = null,

    @SerialName("purchasedSongs")
    val purchasedSongs: List<String> = emptyList(),

    @SerialName("freeSongsReceived")
    val freeSongsReceived: List<String> = emptyList(),

    @SerialName("region")
    val region: String? = null,

    @SerialName("subscription")
    val subscription: SubscriptionNetwork? = null,

    @SerialName("createdAt")
    val createdAt: String? = null,

    @SerialName("updatedAt")
    val updatedAt: String? = null,

    @SerialName("blockedUsers")
    val blockedUsers: List<String> = emptyList(),

    @SerialName("artistIdPostPermission")
    val artistIdPostPermission: String? = null,

    @SerialName("artistsPostPermission")
    val artistsPostPermission: List<String> = emptyList(),
) {
    fun toUserInfo(): UserInfo {
        return UserInfo(
            userId = userId,
            encryptionKey = encryptionKey,
            purchasedSongs = purchasedSongs,
            freeSongsReceived = freeSongsReceived,
            region = region,
            subscription = subscription?.toSubscription(),
            createdAt = createdAt,
            updatedAt = updatedAt,
            blockedUsers = blockedUsers,
            artistIdPostPermission = artistIdPostPermission,
            artistsPostPermission = artistsPostPermission,
        )
    }
}

@Serializable
data class SubscriptionNetwork(
    @SerialName("active")
    val active: Boolean = false,
) {
    fun toSubscription(): Subscription {
        return Subscription(
            active = active,
        )
    }
}
