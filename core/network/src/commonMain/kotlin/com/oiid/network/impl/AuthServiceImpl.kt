package com.oiid.network.impl

import co.touchlab.kermit.Logger
import com.liftric.cognito.idp.IdentityProviderClient
import com.liftric.cognito.idp.core.AuthenticationResult
import com.liftric.cognito.idp.core.UserAttribute
import com.oiid.core.common.APIConfig.Companion.apiConfig
import com.oiid.core.model.BearerTokenResult
import com.oiid.network.AuthResult
import com.oiid.network.AuthService

class AuthServiceImpl() : AuthService {
    private val provider = IdentityProviderClient(apiConfig().region, apiConfig().clientId)

    override suspend fun login(username: String, password: String): AuthResult {
        val result = provider.signIn(username, password)
        return if (result.isSuccess) {
            val authResult = result.getOrThrow().AuthenticationResult
            if (authResult != null) {
                AuthResult(tokens = authResult.toTokens(), success = true)
            } else {
                AuthResult(success = false, message = "Login failed: Missing tokens.")
            }
        } else {
            AuthResult(success = false, message = result.exceptionOrNull()?.message ?: "Unknown error")
        }
    }

    fun AuthenticationResult.toTokens(oldRefreshToken: String? = null): BearerTokenResult? {
        return BearerTokenResult(
            accessToken = AccessToken,
            idToken = IdToken,
            refreshToken = RefreshToken ?: oldRefreshToken,
        )
    }

    override suspend fun signOut(token: String?) {
        if (token == null) return
        val result = provider.signOut(token)
        Logger.d { "SignOut succeeded: $result" }
    }

    override suspend fun signUp(username: String, password: String): AuthResult {
        val result = provider.signUp(username, password, attributes = listOf(UserAttribute("email", username)))
        return if (result.isSuccess) {
            AuthResult(success = true)
        } else {
            AuthResult(success = false, message = result.exceptionOrNull()?.message ?: "Unknown error")
        }
    }

    override suspend fun confirmSignUp(
        username: String,
        confirmation: String,
    ): AuthResult {
        val result = provider.confirmSignUp(username, confirmation)
        return if (result.isSuccess) {
            AuthResult(success = true)
        } else {
            AuthResult(success = false, message = result.exceptionOrNull()?.message ?: "Unknown error")
        }
    }

    override suspend fun forgotPassword(username: String): AuthResult {
        val result = provider.forgotPassword(username)
        return if (result.isSuccess) {
            AuthResult(success = true)
        } else {
            AuthResult(success = false, message = result.exceptionOrNull()?.message ?: "Unknown error")
        }
    }

    override suspend fun refreshToken(refreshToken: String, token: String?): AuthResult {
        val result = provider.refresh(refreshToken)
        if (result.isSuccess) {
            val authResult = result.getOrThrow().AuthenticationResult
            if (authResult != null) {
                return AuthResult(success = true, tokens = authResult.toTokens(refreshToken))
            }
        }

        signOut(token)
        return AuthResult(success = false, message = "Failed to refresh tokens. User has been logged out.")
    }

    override suspend fun resetPassword(
        username: String,
        password: String,
        confirmationCode: String,
    ): AuthResult {
        val result =
            provider.confirmForgotPassword(
                username = username,
                password = password,
                confirmationCode = confirmationCode,
            )
        return if (result.isSuccess) {
            AuthResult(success = true)
        } else {
            AuthResult(success = false, message = result.exceptionOrNull()?.message ?: "Unknown error")
        }
    }

    override suspend fun deleteAccount(token: String?): AuthResult {
        if (token == null) return AuthResult(success = false, message = "Token is required to delete account")

        val result = provider.deleteUser(token)
        return if (result.isSuccess) {
            signOut(token)
            AuthResult(success = true)
        } else {
            AuthResult(success = false, message = result.exceptionOrNull()?.message ?: "Failed to delete account")
        }
    }
}
