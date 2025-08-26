package com.oiid.core.model

data class BearerTokenResult(
    val accessToken: String?,
    val idToken: String?,
    val refreshToken: String?,
)

data class UserCredentials(
    val username: String,
    val password: String,
)

enum class AuthState {
    Refreshing,
    Unauthenticated,
    Authenticated,
}
