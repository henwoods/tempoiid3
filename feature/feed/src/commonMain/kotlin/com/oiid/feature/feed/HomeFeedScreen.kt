package com.oiid.feature.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oiid.core.LocalSnackbarHostState
import com.oiid.core.model.ui.UiEvent
import com.oiid.feature.auth.OnboardingScreen
import com.oiid.feature.feed.list.FeedViewModel
import oiid.core.ui.UiEventHandler
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeFeedScreen(
    modifier: Modifier,
    appBar: @Composable () -> Unit,
    feedViewModel: FeedViewModel = koinViewModel(),
    onPostClicked: (String) -> Unit,
) {
    val isOnboardingCompleted by feedViewModel.isOnboardingCompleted.collectAsState(initial = null)
    val uiState by feedViewModel.uiState.collectAsStateWithLifecycle()
    val selectedItem by feedViewModel.selectedItem.collectAsStateWithLifecycle()

    val showContent by remember {
        derivedStateOf {
            !uiState.isLoading && uiState.error == null
        }
    }

    UiEventHandler(uiEventFlow = feedViewModel.uiEvent)

    BaseFeedScreen(
        modifier = modifier,
        appBar = appBar,
        uiState = uiState,
        showContent = showContent,
        selectedItem = selectedItem,
        setHasNavigated = feedViewModel::setHasNavigated,
        onHandleIntent = feedViewModel::handleIntent,
        onPostClicked = onPostClicked,
    )

    when (isOnboardingCompleted) {
        null, true -> {}
        false -> {
            OnboardingScreen(onDismiss = feedViewModel::setOnboardingComplete)
        }
    }
}
