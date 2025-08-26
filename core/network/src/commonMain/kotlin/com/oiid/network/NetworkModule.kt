package com.oiid.network

import co.touchlab.kermit.Logger
import com.oiid.core.common.APIConfig.Companion.apiConfig
import com.oiid.core.datastore.SettingsTokenStorage
import com.oiid.core.datastore.TokenStorage
import com.oiid.network.api.AuthApiService
import com.oiid.network.api.EventsApiService
import com.oiid.network.api.FanzoneApiService
import com.oiid.network.api.UserApiService
import com.oiid.network.api.createAuthApiService
import com.oiid.network.api.createEventsApiService
import com.oiid.network.api.createFanzoneApiService
import com.oiid.network.api.createUserApiService
import com.oiid.network.impl.AuthServiceImpl
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.logging.LogLevel
import oiid.network.httpClient
import oiid.network.setupDefaultHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

val NetworkModule = module {
    val baseUrl = apiConfig().baseUrl
    val host = apiConfig().hostUrl

    single<TokenStorage> { SettingsTokenStorage(get(), get(named("ApplicationScope"))) }

    single<HttpClient> {
        val tokenStorage = get<TokenStorage>()
        val authService = get<AuthService>()

        httpClient(
            setupDefaultHttpClient(
                baseUrl = baseUrl,
                authRequiredUrl = listOf(host),
                defaultHeaders = mapOf("x-api-key" to apiConfig().xApiKey),
                bearerTokensProvider = {
                    val accessToken = tokenStorage.getAccessToken()
                    val idToken = tokenStorage.getIdToken()
                    val refreshToken = tokenStorage.getRefreshToken()

                    if (accessToken != null && idToken != null) {
                        BearerTokens(idToken, refreshToken)
                    } else {
                        null
                    }
                },
                loggableHosts = listOf(host),
                httpLogger = KermitLogger(),
                httpLogLevel = LogLevel.NONE,
                bearerRefreshProvider = {
                    val refreshToken = tokenStorage.getRefreshToken()

                    if (refreshToken == null) {
                        null
                    } else {
                        val result = authService.refreshToken(refreshToken, tokenStorage.getAccessToken())

                        val tokens = result.tokens
                        val idToken = tokens?.idToken
                        val refreshToken = tokens?.refreshToken

                        if (tokens != null && idToken != null && refreshToken != null) {
                            tokenStorage.saveTokens(result.tokens)
                            BearerTokens(idToken, refreshToken)
                        } else {
                            Logger.d("Failed to refresh token, clearing credentials.")
                            tokenStorage.clearTokens()
                            null
                        }
                    }
                },
            ),
        )
    }

    single {
        Ktorfit.Builder().baseUrl(baseUrl).httpClient(get<HttpClient>()).build()
    }
    single<AuthApiService> { get<Ktorfit>().createAuthApiService() }
    single<AuthService> { AuthServiceImpl() }
    single<UserApiService> { get<Ktorfit>().createUserApiService() }
    single<EventsApiService> { get<Ktorfit>().createEventsApiService() }
    single<FanzoneApiService> { get<Ktorfit>().createFanzoneApiService() }
}

private class KermitLogger : io.ktor.client.plugins.logging.Logger {
    override fun log(message: String) {
        Logger.d("HttpClient: $message")
    }
}
