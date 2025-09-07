package com.oiid.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.oiid.core.data.Links.Companion.PRIVACY_POLICY
import com.oiid.core.data.Links.Companion.TOS
import com.oiid.feature.auth.OnboardingScreen
import oiid.core.base.designsystem.AppStateViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onLogoutClick: () -> Unit,
    appViewModel: AppStateViewModel = koinViewModel(),
    profileViewModel: ProfileViewModel = koinViewModel(),
) {
    val loggingOut = profileViewModel.loggingOut.collectAsState().value
    val deletingAccount = profileViewModel.deletingAccount.collectAsState().value
    val profileUiState = profileViewModel.profileUiState.collectAsState().value
    val editProfileState = profileViewModel.editProfileState.collectAsState().value
    var showOnboarding by remember { mutableStateOf(false) }

    var showThemeDialog by rememberSaveable { mutableStateOf(false) }
    if (showThemeDialog) {
        ThemeDialog(
            onDismiss = { showThemeDialog = false },
        )
    }

    LaunchedEffect(deletingAccount) {
        if (deletingAccount) {
            appViewModel.setForegroundBlur("Deleting account")
        }
    }

    LaunchedEffect(loggingOut) {
        if (loggingOut) {
            appViewModel.setForegroundBlur("Logging out")
        }
    }

    val uriHandler = LocalUriHandler.current

    if (editProfileState.isEditing) {
        EditingProfileScreen(
            onUpdateEditableName = profileViewModel::updateEditableName,
            onUpdateEditableBio = profileViewModel::updateEditableBio,
            onSaveProfileChanges = profileViewModel::saveProfileChanges,
            onCancelEditing = profileViewModel::cancelEditing,
            editProfileState = editProfileState,
            onImagePicked = profileViewModel::onImagePicked,
        )
    } else {
        var showDeleteAccountDialog by rememberSaveable { mutableStateOf(false) }
        if (showDeleteAccountDialog) {
            DeleteAccountDialog(
                onDismiss = { showDeleteAccountDialog = false },
                onConfirm = {
                    profileViewModel.onDeleteAccount {
                        onLogoutClick()
                        appViewModel.setForegroundBlur(null)
                    }
                    showDeleteAccountDialog = false
                },
            )
        }

        ProfileScreenContent(
            modifier = modifier,
            profileUiState = profileUiState,
            onLogoutClick = {
                profileViewModel.onLogout {
                    onLogoutClick()
                }
            },
            onEditProfileClick = profileViewModel::onEditProfileClick,
            onTermsClick = {
                uriHandler.openUri(TOS)
            },
            onPrivacyClick = {
                uriHandler.openUri(PRIVACY_POLICY)
            },
            onOnboardingClick = { showOnboarding = true },
            onDeleteAccountClick = { showDeleteAccountDialog = true },
        )
    }

    if (showOnboarding) {
        OnboardingScreen(
            onDismiss = {
                showOnboarding = false
            },
        )
    }
}
