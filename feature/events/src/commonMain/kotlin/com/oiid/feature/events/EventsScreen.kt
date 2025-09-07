package com.oiid.feature.events

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oiid.core.designsystem.composable.FeedErrorPanel
import com.oiid.core.designsystem.composable.FeedPanel
import com.oiid.core.designsystem.composable.FeedProgress
import com.oiid.core.designsystem.composable.rememberDelayedLoadState
import oiid.core.base.designsystem.theme.OiidTheme.typography
import oiid.core.ui.UiEventHandler
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EventsScreen(
    modifier: Modifier = Modifier,
    appBar: @Composable () -> Unit,
    eventsViewModel: EventsViewModel = koinViewModel(),
) {
    val uiState by eventsViewModel.uiState.collectAsStateWithLifecycle()

    val loadState = rememberDelayedLoadState(uiState.isInitialLoading, uiState.isRefreshing)

    val showContent by remember {
        derivedStateOf {
            !uiState.isLoading && uiState.error == null
        }
    }

    UiEventHandler(uiEventFlow = eventsViewModel.uiEvent)

    Scaffold(modifier = modifier) { paddingValues ->
        FeedPanel(
            paddingValues = paddingValues,
            loadState = loadState.value,
            appBar = appBar,
            content = {
                if (loadState.value.showProgress) {
                    FeedProgress(text = "Loading Events...", size = 84.dp)
                } else {
                    // TODO: test that the button shows correctly
                    FeedErrorPanel(
                        error = uiState.error,
                        onRetryClick = {
                            eventsViewModel.handleIntent(EventsIntent.RetryLoad)
                        },
                    )

                    AnimatedVisibility(
                        visible = showContent,
                        enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                        exit = fadeOut(),
                    ) {
                        if (uiState.events.isEmpty()) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(text = "No future events found", style = typography.headlineMedium)
                            }
                        } else {
                            EventsList(
                                events = uiState.events,
                                onAction = eventsViewModel::handleIntent,
                            )
                        }
                    }
                }
            },
        )
    }
}
