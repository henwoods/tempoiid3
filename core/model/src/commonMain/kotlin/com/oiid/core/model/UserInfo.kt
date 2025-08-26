package com.oiid.core.model

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val userId: String,
    val encryptionKey: String? = null,
    val purchasedSongs: List<String> = emptyList(),
    val freeSongsReceived: List<String> = emptyList(),
    val region: String? = null,
    val subscription: Subscription? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val blockedUsers: List<String> = emptyList(),
    val artistIdPostPermission: String? = null,
    val artistsPostPermission: List<String> = emptyList(),
)

@Serializable
data class Subscription(
    val active: Boolean = false,
)
