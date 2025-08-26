package com.oiid.core.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

data class Profile(
    val id: String? = null,
    val name: String? = null,
    val bio: String? = null,
    val profileImage: String? = null,
    val headerImage: String? = null,
    val updatedAt: Instant = Clock.System.now(),
    val isCurrentUser: Boolean = false,
)

@Serializable
data class ProfileResponse(
    val id: String? = null,
    val name: String? = null,
    val bio: String? = null,
    val profileImage: String? = null,
    val headerImage: String? = null,
    val updatedAt: Instant = Clock.System.now(),
)

@Serializable
data class UpdateProfileRequest(
    val name: String? = null,
    val bio: String? = null,
    val profileImage: String? = null,
    val headerImage: String? = null,
)

fun ProfileResponse.toProfile(isCurrentUser: Boolean = false): Profile {
    return Profile(
        id = id,
        name = name,
        bio = bio,
        profileImage = profileImage,
        headerImage = headerImage,
        updatedAt = updatedAt,
        isCurrentUser = isCurrentUser,
    )
}
