package com.oiid.core.data.auth.impl

import co.touchlab.kermit.Logger
import com.oiid.core.data.auth.AuthOperationResult
import com.oiid.core.data.auth.AuthRepository
import com.oiid.core.datastore.TokenStorage
import com.oiid.core.model.UserCredentials
import com.oiid.network.AuthService
import com.oiid.network.api.AuthApiService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider

class AuthRepositoryImpl(
    private val networkAuthService: AuthService,
    private val authApiService: AuthApiService,
    private val tokenStorage: TokenStorage,
    private val httpClient: HttpClient,
) : AuthRepository {
    override suspend fun signIn(credentials: UserCredentials): AuthOperationResult {
        val authResult = networkAuthService.login(credentials.username, credentials.password)

        return if (authResult.success) {
            val tokens = authResult.tokens
            if (tokens == null) {
                Logger.Companion.e("Successful log in but no tokens returned")
                return AuthOperationResult.Error("Login failed")
            }

            tokenStorage.saveTokens(tokens)
            AuthOperationResult.Success
        } else {
            AuthOperationResult.Error(authResult.message ?: "Login failed")
        }
    }

    override suspend fun signOut() {
        val tokens = tokenStorage.getTokens()
        tokens?.let {
            try {
                networkAuthService.signOut(it.accessToken)
                Logger.Companion.d("SignOut successful")
                deleteSession()
            } catch (e: Exception) {
                Logger.Companion.e("SignOut failed, proceeding with local cleanup $e")
            }
        }

        invalidateAuthTokens()
        tokenStorage.clearTokens()
        Logger.Companion.d("SignOut Cleared tokens.")
    }

    private suspend fun deleteSession() {
        try {
            authApiService.deleteSession()
            Logger.Companion.d("Delete session successful")
        } catch (e: Exception) {
            Logger.Companion.e(e) { "Failed to delete session" }
        }
    }

    fun invalidateAuthTokens() {
        // Need to clear the ktor cache as well, otherwise subsequent logins will reuse the previous
        // users token.
        val authProvider = httpClient.authProvider<BearerAuthProvider>()
        requireNotNull(authProvider)
        authProvider.clearToken()
    }

    override suspend fun signUp(credentials: UserCredentials): AuthOperationResult {
        val result = networkAuthService.signUp(credentials.username, credentials.password)
        return if (result.success) {
            AuthOperationResult.Success
        } else {
            AuthOperationResult.Error(result.message ?: "Sign up failed")
        }
    }

    override suspend fun confirmSignup(
        username: String,
        confirmation: String,
    ): AuthOperationResult {
        val result = networkAuthService.confirmSignUp(username, confirmation)
        return if (result.success) {
            AuthOperationResult.Success
        } else {
            AuthOperationResult.Error(result.message ?: "Sign up failed")
        }
    }

    override suspend fun forgotPassword(username: String): AuthOperationResult {
        val result = networkAuthService.forgotPassword(username)
        return if (result.success) {
            AuthOperationResult.Success
        } else {
            AuthOperationResult.Error(result.message ?: "Sign up failed")
        }
    }

    override suspend fun resetPassword(
        username: String,
        password: String,
        confirmationCode: String,
    ): AuthOperationResult {
        val result = networkAuthService.resetPassword(username, password, confirmationCode)
        return if (result.success) {
            AuthOperationResult.Success
        } else {
            AuthOperationResult.Error(result.message ?: "Sign up failed")
        }
    }

    override suspend fun deleteAccount(): AuthOperationResult {
        val currentTokens = tokenStorage.getTokens()
        if (currentTokens == null) {
            Logger.Companion.e("No tokens found for account deletion")
            return AuthOperationResult.Error("Not authenticated")
        }

        deleteSession()

        val result = networkAuthService.deleteAccount(currentTokens.accessToken)
        return if (result.success) {
            // Clean up local data
            tokenStorage.clearTokens()
            AuthOperationResult.Success
        } else {
            AuthOperationResult.Error(result.message ?: "Failed to delete account")
        }
    }
}
