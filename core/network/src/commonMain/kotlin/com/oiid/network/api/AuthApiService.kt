package com.oiid.network.api

import de.jensklingenberg.ktorfit.http.DELETE

interface AuthApiService {
    @DELETE("app/users/sign-out")
    suspend fun deleteSession()
}
