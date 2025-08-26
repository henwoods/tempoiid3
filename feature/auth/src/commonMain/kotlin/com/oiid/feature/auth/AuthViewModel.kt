package com.oiid.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.oiid.core.data.auth.AuthOperationResult
import com.oiid.core.data.auth.AuthRepository
import com.oiid.core.datastore.TokenStorage
import com.oiid.core.model.AuthState
import com.oiid.core.model.UserCredentials
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    val authRepository: AuthRepository,
    tokenStorage: TokenStorage,
) : ViewModel() {
    private val _authUiState = MutableStateFlow<AuthUiState>(AuthUiState.Success(state = AuthState.Unauthenticated))
    val authUiState: StateFlow<AuthUiState> = _authUiState.asStateFlow()

    private val _authUiStep = MutableStateFlow<AuthStep>(AuthStep.InputDetails)
    val authUiStep: StateFlow<AuthStep> = _authUiStep.asStateFlow()

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmation = MutableStateFlow("")
    val confirmation: StateFlow<String> = _confirmation.asStateFlow()

    private val _passwordResetConfirmed = MutableStateFlow(false)
    val passwordResetConfirmed: StateFlow<Boolean> = _passwordResetConfirmed.asStateFlow()

    val isAuthenticated = tokenStorage.isAuthenticated

    fun updateUsername(value: String) {
        _username.value = value
        clearErrorState()
    }

    fun updatePassword(value: String) {
        _password.value = value
        clearErrorState()
    }

    fun updateConfirmation(value: String) {
        _confirmation.value = value
        clearErrorState()
    }

    private fun clearErrorState() {
        if (_authUiState.value is AuthUiState.Error) {
            _authUiState.update { AuthUiState.Success(state = AuthState.Unauthenticated) }
        }
    }

    fun resetPasswordFields() {
        _password.value = ""
        _confirmation.value = ""
    }

    private fun validateEmail(email: String): String? {
        if (email.isEmpty()) {
            return "Email cannot be empty"
        }

        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        if (!emailRegex.matches(email)) {
            return "Invalid email format"
        }

        return null
    }

    private fun validatePassword(password: String): String? {
        if (password.isEmpty()) {
            return "Password cannot be empty"
        }

        if (password.length < 8) {
            return "Password must be at least 8 characters long"
        }

        val hasUppercase = password.any { it.isUpperCase() }
        val hasLowercase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }

        return when {
            !hasUppercase -> "Password must contain at least one uppercase letter"
            !hasLowercase -> "Password must contain at least one lowercase letter"
            !hasDigit -> "Password must contain at least one number"
            else -> null
        }
    }

    fun validate(username: String? = null, password: String? = null): Boolean {
        if (username != null) {
            val emailError = validateEmail(username)
            if (emailError != null) {
                _authUiState.value = AuthUiState.Error(emailError)
                return false
            }
        }

        if (password != null) {
            val passwordError = validatePassword(password)
            if (passwordError != null) {
                _authUiState.value = AuthUiState.Error(passwordError)
                return false
            }
        }

        return true
    }

    fun signIn() {
        val currentUsername = username.value
        val currentPassword = password.value

        if (!validate(currentUsername, currentPassword)) return

        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading(
                state = AuthLoadingState.SigningIn,
            )

            val result = authRepository.signIn(UserCredentials(currentUsername, currentPassword))

            if (result is AuthOperationResult.Error) {
                Logger.e("Login error ${result.message}")

                val errorMessage = result.message
                val message = if (errorMessage == null) {
                    "Something went wrong logging in"
                } else if (errorMessage.contains("Incorrect username or password")) {
                    "Incorrect username or password"
                } else if (errorMessage.contains("An account with the given email already exists")) {
                    "An account with the given email already exists"
                } else {
                    "Something went wrong logging in"
                }

                _authUiState.value = AuthUiState.Error(message)
            } else {
                _authUiState.value = AuthUiState.Success(state = AuthState.Authenticated)
            }
        }
    }

    fun signUp() {
        val currentUsername = username.value
        val currentPassword = password.value

        if (!validate(currentUsername, currentPassword)) return

        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading(
                state = AuthLoadingState.SigningUp,
            )

            val result = authRepository.signUp(UserCredentials(currentUsername, currentPassword))

            if (result is AuthOperationResult.Error) {
                Logger.e("Signup error ${result.message}")
                _authUiState.value = AuthUiState.Error("Something went wrong signing up")
            } else {
                _authUiState.value = AuthUiState.Success(state = AuthState.Unauthenticated)
                moveToStep(AuthStep.WaitingConfirmation)
            }
        }
    }

    fun onForgotPassword(username: String) {
        if (!validate(username)) return

        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading(
                state = AuthLoadingState.SigningUp,
            )

            val result = authRepository.forgotPassword(username)

            if (result is AuthOperationResult.Error) {
                Logger.e("Password reset error ${result.message}")
                _authUiState.value = AuthUiState.Error("Something went wrong with password reset")
            } else {
                _authUiState.value = AuthUiState.Success(state = AuthState.Unauthenticated)
                moveToStep(AuthStep.WaitingConfirmation)
            }
        }
    }

    fun finishResetPassword(username: String, password: String, confirmationCode: String) {
        if (!validate(username, password)) return

        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading(
                state = AuthLoadingState.ResettingPassword,
            )
            val result = authRepository.resetPassword(username, password, confirmationCode)
            if (result is AuthOperationResult.Error) {
                Logger.e("Password reset error ${result.message}")
                _authUiState.value = AuthUiState.Error("Something went wrong with password reset")
            } else {
                _authUiState.value = AuthUiState.Success(state = AuthState.Unauthenticated)
                resetPasswordFields()
                _passwordResetConfirmed.update { true }
                moveToStep(AuthStep.InputDetails)
            }
        }
    }

    fun confirmResetPassword() {
        val currentUsername = username.value
        val currentPassword = password.value
        val currentConfirmation = confirmation.value
        finishResetPassword(currentUsername, currentPassword, currentConfirmation)
    }

    fun onForgotPasswordClick() {
        clearErrorState()
        resetPasswordFields()
        moveToStep(AuthStep.ForgotPassword)
    }

    fun cancelResetPassword() {
        clearErrorState()
        _passwordResetConfirmed.update { false }
        moveToStep(AuthStep.InputDetails)
    }

    fun startPasswordReset() {
        val currentUsername = username.value
        onForgotPassword(currentUsername)
    }

    fun onConfirmSignup(onRegistrationConfirmed: () -> Unit) {
        val currentUsername = username.value
        val currentConfirmation = confirmation.value

        if (!validate(currentUsername)) return

        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading(state = AuthLoadingState.SigningUp)

            val result = authRepository.confirmSignup(currentUsername, currentConfirmation)
            if (result is AuthOperationResult.Error) {
                Logger.e("Sign up confirmation error ${result.message}")

                val errorMessage = result.message
                val message = if (errorMessage == null) {
                    "Something went wrong confirming sign up"
                } else if (errorMessage.contains("Invalid verification code provided, please try again")) {
                    "Invalid code, please try again"
                } else {
                    "Something went wrong confirming sign up"
                }

                _authUiState.value = AuthUiState.Error(message)
            } else {
                onRegistrationConfirmed()
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading(
                state = AuthLoadingState.LoggingOut,
            )

            authRepository.signOut()

            _authUiState.value = AuthUiState.Success(
                state = AuthState.Unauthenticated,
            )
        }
    }

    fun moveToStep(step: AuthStep) {
        _authUiStep.value = step

        if (step is AuthStep.InputDetails) {
            updatePassword("")
        }
    }
}

enum class AuthLoadingState {
    SigningIn,
    SigningUp,
    LoggingOut,
    ResettingPassword,
}

sealed interface AuthUiState {
    data class Loading(val state: AuthLoadingState) : AuthUiState
    data class Error(val message: String) : AuthUiState
    data class Success(val state: AuthState) : AuthUiState
}

sealed interface AuthStep {
    data object InputDetails : AuthStep
    data object ForgotPassword : AuthStep
    data object WaitingConfirmation : AuthStep
}
