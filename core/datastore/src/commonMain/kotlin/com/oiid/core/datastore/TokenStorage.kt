package com.oiid.core.datastore

import com.oiid.core.model.AuthState
import com.oiid.core.model.BearerTokenResult
import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface TokenStorage {
    val isAuthenticated: StateFlow<AuthState>
    suspend fun saveTokens(tokens: BearerTokenResult)
    suspend fun getAccessToken(): String?
    suspend fun getIdToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun getTokens(): BearerTokenResult?
    suspend fun clearTokens()
}

class SettingsTokenStorage(
    private val settings: Settings,
    private val appScope: CoroutineScope,
) : TokenStorage {
    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_ID_TOKEN = "id_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }

    private val _isAuthenticated = MutableStateFlow(AuthState.Refreshing)
    override val isAuthenticated: StateFlow<AuthState> = _isAuthenticated.asStateFlow()

    init {
        appScope.launch {
            _isAuthenticated.value = if (!getIdToken().isNullOrEmpty()) AuthState.Authenticated else AuthState.Unauthenticated
        }
    }

    override suspend fun saveTokens(tokens: BearerTokenResult) {
        val accessToken = tokens.accessToken
        if (accessToken != null) {
            settings.putString(KEY_ACCESS_TOKEN, accessToken)
        }

        val idToken = tokens.idToken
        if (idToken != null) {
            settings.putString(KEY_ID_TOKEN, idToken)
        }

        val refreshToken = tokens.refreshToken
        if (refreshToken != null) {
            settings.putString(KEY_REFRESH_TOKEN, refreshToken)
        }

        _isAuthenticated.value = AuthState.Authenticated
    }

    override suspend fun getAccessToken(): String? = settings.getStringOrNull(KEY_ACCESS_TOKEN)
    override suspend fun getIdToken(): String? = settings.getStringOrNull(KEY_ID_TOKEN)
    override suspend fun getRefreshToken(): String? = settings.getStringOrNull(KEY_REFRESH_TOKEN)

    override suspend fun getTokens(): BearerTokenResult? {
        val access = getAccessToken()
        val id = getIdToken()
        val refresh = getRefreshToken()
        return if (access != null && id != null && refresh != null) {
            BearerTokenResult(access, id, refresh)
        } else {
            null
        }
    }

    override suspend fun clearTokens() {
        settings.remove(KEY_ACCESS_TOKEN)
        settings.remove(KEY_ID_TOKEN)
        settings.remove(KEY_REFRESH_TOKEN)
        _isAuthenticated.value = AuthState.Unauthenticated
    }
}
