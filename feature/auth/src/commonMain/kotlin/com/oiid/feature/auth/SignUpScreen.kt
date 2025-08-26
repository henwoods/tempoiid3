package com.oiid.feature.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import co.touchlab.kermit.Logger
import com.oiid.core.data.Links.Companion.PRIVACY_POLICY
import com.oiid.core.data.Links.Companion.TOS
import com.oiid.core.designsystem.composable.AuthButton
import com.oiid.core.designsystem.composable.AuthContainer
import com.oiid.core.designsystem.composable.AuthStack
import com.oiid.core.designsystem.composable.AuthTextButton
import com.oiid.core.designsystem.composable.AuthTextField
import com.oiid.core.designsystem.composable.InfoTextPanel
import com.oiid.core.designsystem.composable.ScreenWithBackground
import com.oiid.core.designsystem.composable.TextPanel
import com.oiid.core.oiidPainterResource
import oiid.core.base.designsystem.FullScreenBackground
import oiid.core.base.designsystem.theme.OiidTheme
import org.koin.compose.viewmodel.koinViewModel

const val SIGNUP_BACKGROUND_KEY = "registration"

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onConfirmedLogin: () -> Unit,
    viewModel: AuthViewModel = koinViewModel(),
) {
    val uriHandler = LocalUriHandler.current
    val state = viewModel.authUiState.collectAsState()
    val uiState = state.value

    ScreenWithBackground(
        key = SIGNUP_BACKGROUND_KEY,
        background = FullScreenBackground.Paint(oiidPainterResource("welcome_1")),
    ) {
        SignUpScreenContent(
            modifier = modifier.fillMaxSize(),
            authUiState = uiState,
            onSignUpClick = { -> viewModel.signUp() },
            onCancelClick = onBackClick,
            onTermsClick = {
                uriHandler.openUri(TOS)
            },
            onPrivacyClick = {
                uriHandler.openUri(PRIVACY_POLICY)
            },
            onConfirmedLogin = onConfirmedLogin,
            viewModel = viewModel,
        )
    }
}

@Composable
internal fun SignUpScreenContent(
    modifier: Modifier = Modifier,
    authUiState: AuthUiState,
    onSignUpClick: () -> Unit,
    onCancelClick: () -> Unit,
    onConfirmedLogin: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    viewModel: AuthViewModel,
) {
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmation by viewModel.confirmation.collectAsState()

    val authStep = viewModel.authUiStep.collectAsState()
    val isLoading = authUiState is AuthUiState.Loading
    val progress = authProgressPanel(authUiState)

    var isTermsOfServiceChecked by remember { mutableStateOf(false) }

    val isValid = if (authStep.value == AuthStep.WaitingConfirmation) {
        username.isNotEmpty() && confirmation.isNotEmpty() && isTermsOfServiceChecked
    } else {
        username.isNotEmpty() && password.isNotEmpty() && isTermsOfServiceChecked
    }

    AuthContainer(
        modifier = modifier,
        title = { AuthHeader() },
        fields = {
            AuthStack {
                AnimatedContent(
                    targetState = authStep,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                ) { step ->
                    when (step.value) {
                        is AuthStep.InputDetails -> {
                            AuthStack {
                                AuthTextField(
                                    value = username,
                                    onValueChange = { viewModel.updateUsername(it) },
                                    enabled = !isLoading,
                                    label = "Email address",
                                )

                                AuthStack {
                                    AuthTextField(
                                        value = password,
                                        onValueChange = { viewModel.updatePassword(it) },
                                        enabled = !isLoading,
                                        isPassword = true,
                                        label = "Password",
                                        type = KeyboardType.Password,
                                        onDone = viewModel::signUp,
                                    )

                                    Spacer(modifier = Modifier.height(OiidTheme.spacing.xs))

                                    AnimatedVisibility(progress != null) {
                                        if (progress != null) {
                                            Column {
                                                progress()
                                            }
                                        }
                                    }

                                    AuthTermsAndConditionsPanel(
                                        isTermsOfServiceChecked = isTermsOfServiceChecked,
                                        isLoading = isLoading,
                                        onTermsCheckedChange = {
                                            isTermsOfServiceChecked = !isTermsOfServiceChecked
                                        },
                                        onTermsClick = onTermsClick,
                                        onPrivacyClick = onPrivacyClick,
                                    )
                                }
                            }
                        }

                        AuthStep.ForgotPassword -> {
                        }

                        AuthStep.WaitingConfirmation -> {
                            AuthStack {
                                AuthTextField(
                                    value = username,
                                    onValueChange = { viewModel.updateUsername(it) },
                                    enabled = false,
                                    label = "Email address",
                                )

                                AuthStack {
                                    InfoTextPanel(
                                        message = "Please check your email for the confirmation code",
                                        backgroundColor = OiidTheme.colorScheme.surface.copy(alpha = .7f),
                                    )
                                    AuthTextField(
                                        value = confirmation,
                                        onValueChange = viewModel::updateConfirmation,
                                        enabled = !isLoading,
                                        label = "Confirmation code",
                                        type = KeyboardType.Number,
                                        onDone = {
                                            viewModel.onConfirmSignup { onConfirmedLogin() }
                                        },
                                    )
                                }

                                AnimatedVisibility(progress != null) {
                                    if (progress != null) {
                                        Column {
                                            progress()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        buttons = {
            AuthStack {
                when (authStep.value) {
                    AuthStep.ForgotPassword -> {
                    }

                    is AuthStep.InputDetails -> {
                        AuthButton(
                            onClick = onSignUpClick,
                            enabled = isValid && !isLoading,
                            text = "Sign Up",
                        )

                        AuthButton(onClick = onCancelClick, enabled = !isLoading, text = "Cancel")
                    }

                    AuthStep.WaitingConfirmation -> {
                        AuthButton(
                            onClick = { viewModel.onConfirmSignup { onConfirmedLogin() } },
                            enabled = isValid && !isLoading,
                            text = "Confirm",
                        )

                        AuthButton(onClick = onCancelClick, enabled = !isLoading, text = "Cancel")
                    }
                }
            }
        },
    )
}

@Composable
fun AuthTermsAndConditionsPanel(
    isTermsOfServiceChecked: Boolean,
    isLoading: Boolean,
    onTermsCheckedChange: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
) {
    val termsOfServiceIcon = if (isTermsOfServiceChecked) {
        Icons.Default.CheckBox
    } else {
        Icons.Default.CheckBoxOutlineBlank
    }

    TextPanel(
        backgroundColor = OiidTheme.colorScheme.surface,
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(
                    start = OiidTheme.spacing.md,
                ),
            ) {
                Icon(
                    imageVector = termsOfServiceIcon,
                    contentDescription = null,
                    tint = OiidTheme.colorScheme.onPrimary,
                    modifier = Modifier.clickable {
                        Logger.d("checkbox click")
                        onTermsCheckedChange()
                    },
                )

                AuthTextButton(
                    onClick = {
                        onTermsCheckedChange()
                    },
                    enabled = !isLoading,
                    content = {
                        TermsAndPrivacyText(
                            onTermsClick = onTermsClick,
                            onPrivacyClick = onPrivacyClick,
                            enabled = !isLoading,
                        )
                    },
                )
            }
        },
    )
}

@Composable
fun TermsAndPrivacyText(
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    enabled: Boolean = true,
) {
    val terms = "Terms of Service"
    val privacy = "Privacy Policy"

    val annotatedText = buildAnnotatedString {
        append("I agree to the ")

        pushStringAnnotation(tag = "TERMS", annotation = "terms")
        withStyle(
            style = SpanStyle(
                textDecoration = TextDecoration.Underline,
            ),
        ) {
            append(terms)
        }
        pop()

        append(" and ")

        pushStringAnnotation(tag = "PRIVACY", annotation = "privacy")
        withStyle(
            style = SpanStyle(
                textDecoration = TextDecoration.Underline,
            ),
        ) {
            append(privacy)
        }
        pop()
    }
    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    val pressIndicator = Modifier.pointerInput(onTermsClick, onPrivacyClick) {
        detectTapGestures { offset ->

            layoutResult?.let { textLayoutResult ->
                val position = textLayoutResult.getOffsetForPosition(offset)
                annotatedText.getStringAnnotations(start = position, end = position).firstOrNull()?.let { annotation ->
                    if (!enabled) return@let

                    when (annotation.tag) {
                        "TERMS" -> onTermsClick()
                        "PRIVACY" -> onPrivacyClick()
                    }
                }
            }
        }
    }

    Text(
        text = annotatedText,
        style = OiidTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
        onTextLayout = {
            layoutResult = it
        },
        modifier = Modifier.then(pressIndicator),
    )
}
