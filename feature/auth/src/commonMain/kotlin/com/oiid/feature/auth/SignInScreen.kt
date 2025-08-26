package com.oiid.feature.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.oiid.core.designsystem.composable.AuthButton
import com.oiid.core.designsystem.composable.AuthContainer
import com.oiid.core.designsystem.composable.AuthStack
import com.oiid.core.designsystem.composable.AuthTextButton
import com.oiid.core.designsystem.composable.AuthTextField
import com.oiid.core.designsystem.composable.InfoTextPanel
import com.oiid.core.designsystem.composable.ScreenWithBackground
import com.oiid.core.designsystem.composable.TextPanel
import com.oiid.core.model.AuthState
import com.oiid.core.model.nav.ConfirmationStateArgs
import com.oiid.core.oiidPainterResource
import kotlinx.coroutines.delay
import oiid.core.base.designsystem.AppStateViewModel
import oiid.core.base.designsystem.FullScreenBackground
import oiid.core.base.designsystem.theme.OiidTheme
import org.koin.compose.viewmodel.koinViewModel

const val SIGN_IN_BACKGROUND_KEY = "signin"

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    confirmationState: ConfirmationStateArgs = ConfirmationStateArgs(confirmed = false),
    onBackClick: () -> Unit,
    viewModel: AuthViewModel = koinViewModel(),
    appStateViewModel: AppStateViewModel = koinViewModel(),
) {
    val state = viewModel.authUiState.collectAsState()

    LaunchedEffect(state.value) {
        val value = state.value
        if (value is AuthUiState.Loading) {
            if (value.state == AuthLoadingState.SigningIn) {
                appStateViewModel.setForegroundBlur("Signing In")
                return@LaunchedEffect
            }
        } else if (value is AuthUiState.Error) {
            appStateViewModel.setForegroundBlur(null)
        } else {
            // Leave the blurred state if it's a successful sign in, it will be removed by the main screen.
        }
    }

    var showSignInContent by remember { mutableStateOf(true) }

    ScreenWithBackground(
        key = SIGN_IN_BACKGROUND_KEY,
        background = FullScreenBackground.Paint(oiidPainterResource("welcome_1")),
    ) {
        val authUiState = state.value
        if (authUiState is AuthUiState.Success && authUiState.state == AuthState.Authenticated) {
            LaunchedEffect(Unit) {
                appStateViewModel.clearBackground(SIGN_IN_BACKGROUND_KEY, null, true)
                showSignInContent = false
                delay(300)
            }
        }

        AnimatedVisibility(
            visible = showSignInContent,
            exit = fadeOut(animationSpec = tween(300)),
        ) {
            SignInScreenContent(
                modifier = modifier.fillMaxSize(),
                confirmationState = confirmationState,
                authUiState = authUiState,
                onCancelClick = onBackClick,
                viewModel = viewModel,
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun SignInScreenContent(
    modifier: Modifier = Modifier,
    confirmationState: ConfirmationStateArgs,
    authUiState: AuthUiState,
    onCancelClick: () -> Unit,
    viewModel: AuthViewModel,
) {
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    val passwordResetCompleted by viewModel.passwordResetConfirmed.collectAsState()
    val confirmation by viewModel.confirmation.collectAsState()
    val step = viewModel.authUiStep.collectAsState()

    val progress = authProgressPanel(authUiState)
    val isLoading = authUiState is AuthUiState.Loading

    val isValid = when (step.value) {
        is AuthStep.InputDetails -> {
            username.isNotEmpty() && password.isNotEmpty()
        }

        AuthStep.ForgotPassword -> {
            username.isNotEmpty()
        }

        AuthStep.WaitingConfirmation -> {
            username.isNotEmpty() && confirmation.isNotEmpty() && password.isNotEmpty()
        }
    }

    AuthContainer(
        modifier = modifier,
        title = { AuthHeader() },
        fields = {
            AuthStack {
                AnimatedContent(
                    targetState = step.value,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                ) { step ->
                    when (step) {
                        AuthStep.ForgotPassword -> {
                            AuthTextField(
                                value = username,
                                onValueChange = {
                                    viewModel.updateUsername(it)
                                },
                                enabled = !isLoading,
                                label = "Enter your email address",
                                onDone = viewModel::startPasswordReset,
                            )
                        }

                        is AuthStep.InputDetails -> AuthStack {
                            if (confirmationState.confirmed) {
                                InfoTextPanel("Email confirmed, continue signing in below")
                            } else if (passwordResetCompleted) {
                                InfoTextPanel("Password reset, continue signing in below")
                            }

                            AuthTextField(
                                value = username,
                                onValueChange = viewModel::updateUsername,
                                enabled = !isLoading,
                                label = "Email",
                            )
                            AuthTextField(
                                value = password,
                                onValueChange = viewModel::updatePassword,
                                enabled = !isLoading,
                                isPassword = true,
                                label = "Password",
                                type = KeyboardType.Password,
                                onDone = viewModel::signIn,
                            )
                        }

                        AuthStep.WaitingConfirmation -> {
                            AuthStack {
                                InfoTextPanel("Check your email for the confirmation code to create a new password.")

                                AuthTextField(
                                    value = confirmation,
                                    onValueChange = {
                                        viewModel.updateConfirmation(it)
                                    },
                                    enabled = !isLoading,
                                    label = "Confirmation code",
                                    type = KeyboardType.Number,
                                )

                                AuthTextField(
                                    value = password,
                                    onValueChange = {
                                        viewModel.updatePassword(it)
                                    },
                                    isPassword = true,
                                    enabled = !isLoading,
                                    label = "New Password",
                                    type = KeyboardType.Password,
                                    onDone = viewModel::confirmResetPassword,
                                )
                            }
                        }
                    }
                }

                AnimatedVisibility(progress != null) {
                    if (progress != null) {
                        Column {
                            progress()
                        }
                    }
                }

                Spacer(modifier = Modifier.height(OiidTheme.spacing.xs))

                val inForgotPasswordStep = step.value == AuthStep.ForgotPassword
                AnimatedVisibility(!inForgotPasswordStep) {
                    TextPanel(
                        backgroundColor = OiidTheme.colorScheme.surface,
                        content = {
                            AuthTextButton(
                                onClick = viewModel::onForgotPasswordClick,
                                enabled = !isLoading,
                                text = "I forgot my password",
                            )
                        },
                    )
                }
            }
        },
        buttons = {
            AuthStack {
                when (step.value) {
                    AuthStep.ForgotPassword -> {
                        AuthButton(
                            onClick = viewModel::startPasswordReset,
                            enabled = !isLoading && isValid,
                            text = "Reset Password",
                        )
                        AuthButton(
                            onClick = viewModel::cancelResetPassword,
                            enabled = !isLoading,
                            text = "Cancel",
                        )
                    }

                    is AuthStep.InputDetails -> {
                        AuthButton(
                            onClick = viewModel::signIn,
                            enabled = !isLoading && isValid,
                            text = "Sign In",
                        )
                        AuthButton(
                            onClick = {
                                onCancelClick()
                            },
                            enabled = !isLoading,
                            text = "Cancel",
                        )
                    }

                    AuthStep.WaitingConfirmation -> {
                        AuthButton(
                            onClick = viewModel::confirmResetPassword,
                            enabled = !isLoading && isValid,
                            text = "Confirm",
                        )
                        AuthButton(
                            onClick = viewModel::cancelResetPassword,
                            enabled = !isLoading,
                            text = "Cancel",
                        )
                    }
                }
            }
        },
    )
}
