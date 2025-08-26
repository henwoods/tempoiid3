package com.oiid.core.data.auth

import com.oiid.core.model.UserCredentials

sealed class AuthOperationResult {
    data object Success : AuthOperationResult()
    data class Error(val message: String?) : AuthOperationResult()
}

interface AuthRepository {
    suspend fun signIn(credentials: UserCredentials): AuthOperationResult
    suspend fun signOut()
    suspend fun signUp(credentials: UserCredentials): AuthOperationResult
    suspend fun confirmSignup(username: String, confirmation: String): AuthOperationResult
    suspend fun forgotPassword(username: String): AuthOperationResult
    suspend fun resetPassword(username: String, password: String, confirmationCode: String): AuthOperationResult
    suspend fun deleteAccount(): AuthOperationResult
}
