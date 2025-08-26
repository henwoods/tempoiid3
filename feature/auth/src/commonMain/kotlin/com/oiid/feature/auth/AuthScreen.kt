package com.oiid.feature.auth

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.oiid.core.designsystem.composable.AuthButton
import com.oiid.core.designsystem.composable.AuthContainer
import com.oiid.core.designsystem.composable.AuthStack
import com.oiid.core.designsystem.composable.ScreenWithBackground
import com.oiid.core.oiidPainterResource
import oiid.core.base.designsystem.AppStateViewModel
import oiid.core.base.designsystem.FullScreenBackground
import org.koin.compose.viewmodel.koinViewModel

const val AUTH_BACKGROUND_KEY = "auth"

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    onNavigateToSignIn: () -> Unit,
    onNavigateToRegistration: () -> Unit,
    appStateViewModel: AppStateViewModel = koinViewModel(),
) {
    LaunchedEffect(Unit) {
        appStateViewModel.setForegroundBlur(null)
    }

    AuthScreenContent(
        modifier = modifier.fillMaxSize(),
        onLoginClick = onNavigateToSignIn,
        onSignupClick = onNavigateToRegistration,
    )
}

@Composable
internal fun AuthScreenContent(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit,
) {
    ScreenWithBackground(
        key = AUTH_BACKGROUND_KEY,
        background = FullScreenBackground.Paint(oiidPainterResource("welcome_1")),
        content = {
            AuthContainer(
                modifier = modifier,
                title = { AuthHeader() },
                buttons = {
                    AuthStack {
                        AuthButton(onClick = onLoginClick, enabled = true, text = "Sign In")
                        AuthButton(onClick = onSignupClick, enabled = true, text = "Sign Up")
                    }
                },
            )
        },
    )
}
