package com.oiid.network

import com.oiid.core.model.BearerTokenResult

data class AuthResult(val success: Boolean, val tokens: BearerTokenResult? = null, val message: String? = null)

interface AuthService {
    suspend fun login(username: String, password: String): AuthResult
    suspend fun signOut(token: String?)
    suspend fun signUp(username: String, password: String): AuthResult
    suspend fun confirmSignUp(username: String, confirmation: String): AuthResult
    suspend fun forgotPassword(username: String): AuthResult
    suspend fun refreshToken(refreshToken: String, token: String?): AuthResult
    suspend fun resetPassword(username: String, password: String, confirmationCode: String): AuthResult
    suspend fun deleteAccount(token: String?): AuthResult
}
