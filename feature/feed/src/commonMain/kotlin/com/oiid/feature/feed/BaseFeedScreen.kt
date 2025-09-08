package com.oiid.feature.feed

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.oiid.core.designsystem.composable.FeedErrorPanel
import com.oiid.core.designsystem.composable.FeedPanel
import com.oiid.core.designsystem.composable.FeedProgress
import com.oiid.core.designsystem.composable.rememberDelayedLoadState
import com.oiid.core.designsystem.diagonalCornerShape
import com.oiid.feature.feed.list.FeedList
import com.oiid.feature.feed.list.FeedListItem
import com.oiid.feature.feed.list.FeedListItemUiState
import com.oiid.feature.feed.list.FeedUiState
import com.oiid.feature.feed.list.NavToPost
import oiid.core.base.designsystem.AppStateViewModel
import oiid.core.base.designsystem.theme.OiidTheme.colorScheme
import oiid.core.ui.ConfirmationDialogs
import oiid.core.ui.FeedIntent
import oiid.core.ui.getDialogContent
import oiid.core.ui.needsConfirmation
import oiid.core.ui.rememberConfirmationDialogHandler
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BaseFeedScreen(
    modifier: Modifier = Modifier,
    appBar: @Composable () -> Unit,
    uiState: FeedUiState,
    backgroundColor: Color = colorScheme.surface,
    divider: @Composable (() -> Unit)? = null,
    selectedItem: NavToPost?,
    showContent: Boolean = true,
    onHandleIntent: (FeedIntent) -> Unit,
    onPostClicked: (String) -> Unit,
    setHasNavigated: (Boolean) -> Unit,
    appStateViewModel: AppStateViewModel = koinViewModel(),
) {
    val confirmationDialogState = rememberConfirmationDialogHandler(
        onHandleIntent = onHandleIntent,
        needsConfirmation = { it.needsConfirmation() },
        getDialogContent = { it.getDialogContent() },
    )

    val lifecycleOwner = LocalLifecycleOwner.current

    val loadState = rememberDelayedLoadState(uiState.isInitialLoading, uiState.isRefreshing)

    LaunchedEffect(selectedItem?.item?.id) {
        selectedItem?.let { selected ->
            if (!selected.hasNavigated) {
                confirmationDialogState.handleIntent(FeedIntent.ItemSelected(selected.item))
                onPostClicked(selected.item.id)
                setHasNavigated(true)
            }
        }
    }

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            appStateViewModel.setForegroundBlur(null)
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (selectedItem != null) {
                    confirmationDialogState.handleIntent(FeedIntent.ItemSelected(null))
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(modifier = modifier) { paddingValues ->
        SharedTransitionLayout {
            AnimatedContent(
                targetState = selectedItem,
                transitionSpec = {
                    if (uiState.isForum) {
                        fadeIn(animationSpec = tween(0)) togetherWith fadeOut(animationSpec = tween(0))
                    } else {
                        fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                    }
                },
                label = "ListToDetail",
            ) { targetItem ->
                if (targetItem == null || uiState.isForum) {
                    FeedPanel(
                        paddingValues = paddingValues,
                        loadState = loadState.value,
                        appBar = appBar,
                        content = {
                            if (loadState.value.showProgress && uiState.isForum) {
                                FeedProgress(text = "Loading Fan-zone...", size = 72.dp)
                            } else {
                                FeedErrorPanel(
                                    error = uiState.error,
                                    onRetryClick = {
                                        confirmationDialogState.handleIntent(FeedIntent.RetryLoad)
                                    },
                                )

                                AnimatedVisibility(
                                    visible = showContent,
                                    enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                                    exit = fadeOut(),
                                ) {
                                    FeedList(
                                        modifier = Modifier.background(backgroundColor),
                                        uiState = uiState,
                                        onHandleIntent = confirmationDialogState::handleIntent,
                                        divider = divider,
                                        sharedTransitionScope = this@SharedTransitionLayout,
                                        animatedVisibilityScope = this@AnimatedContent,
                                    )
                                }
                            }

                        },
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                        FeedListItem(
                            modifier = Modifier.sharedElement(
                                sharedContentState = rememberSharedContentState(key = "item-${targetItem.item.id}"),
                                animatedVisibilityScope = this@AnimatedContent,
                            ).clip(diagonalCornerShape()),
                            uiState = FeedListItemUiState(
                                targetItem.item,
                                isPlaying = false,
                                isForum = uiState.isForum,
                                isDetail = false,
                            ),
                            onHandleIntent = confirmationDialogState::handleIntent,
                        )
                    }
                }
            }
        }
    }

    ConfirmationDialogs(confirmationDialogState)
}
