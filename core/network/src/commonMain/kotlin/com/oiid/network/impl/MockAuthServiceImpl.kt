package com.oiid.network.impl

import com.oiid.network.AuthResult
import com.oiid.network.AuthService

class MockAuthServiceImpl(
    private val simulateError: Boolean = false,
) : AuthService {
    override suspend fun login(username: String, password: String): AuthResult {
        return if (simulateError) {
            AuthResult(success = false, message = "Login failed")
        } else {
            AuthResult(success = true)
        }
    }

    override suspend fun signOut(token: String?) {
        // Not implemented
    }

    override suspend fun signUp(username: String, password: String): AuthResult {
        return if (simulateError) {
            AuthResult(success = true)
        } else {
            AuthResult(success = true)
        }
    }

    override suspend fun confirmSignUp(
        username: String,
        confirmation: String,
    ): AuthResult {
        TODO("Not yet implemented")
    }

    override suspend fun forgotPassword(username: String): AuthResult {
        TODO("Not yet implemented")
    }

    override suspend fun refreshToken(refreshToken: String, token: String?): AuthResult {
        return if (simulateError) {
            AuthResult(success = true)
        } else {
            AuthResult(success = true)
        }
    }

    override suspend fun resetPassword(
        username: String,
        password: String,
        confirmationCode: String,
    ): AuthResult {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccount(token: String?): AuthResult {
        TODO("Not yet implemented")
    }
}
