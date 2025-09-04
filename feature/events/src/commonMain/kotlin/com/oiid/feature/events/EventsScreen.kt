package com.oiid.feature.events

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oiid.core.designsystem.composable.InfoTextPanel
import com.oiid.core.designsystem.composable.LinearProgress
import com.oiid.core.designsystem.composable.OiidTextButton
import oiid.core.ui.UiEventHandler
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EventsScreen(
    modifier: Modifier = Modifier,
    appBar: @Composable () -> Unit,
    eventsViewModel: EventsViewModel = koinViewModel(),
) {
    val uiState by eventsViewModel.uiState.collectAsStateWithLifecycle()

    val showContent by remember {
        derivedStateOf {
            !uiState.isLoading && uiState.error == null
        }
    }

    UiEventHandler(uiEventFlow = eventsViewModel.uiEvent)

    Scaffold(modifier = modifier) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            Column(Modifier.heightIn(max = 56.dp)) {
                appBar()
            }
            if (uiState.isLoading) {
                LinearProgress(modifier = Modifier.fillMaxWidth())
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
            ) {
                uiState.error?.let { error ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        InfoTextPanel(
                            message = error,
                            buttons = {
                                OiidTextButton(
                                    text = "Retry",
                                    onClick = { eventsViewModel.handleIntent(EventsIntent.RetryLoad) },
                                )
                            },
                        )
                    }
                }

                AnimatedVisibility(
                    visible = showContent,
                    enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                    exit = fadeOut(),
                ) {
                    if (uiState.events.isEmpty()) {
                        InfoTextPanel(
                            message = "No events scheduled",
                            buttons = {},
                        )
                    } else {
                        EventsList(
                            events = uiState.events,
                            onAction = eventsViewModel::handleIntent,
                        )
                    }
                }
            }
        }
    }
}
