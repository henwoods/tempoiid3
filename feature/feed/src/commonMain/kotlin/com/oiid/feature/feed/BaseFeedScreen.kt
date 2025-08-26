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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.oiid.core.designsystem.composable.InfoTextPanel
import com.oiid.core.designsystem.composable.LinearProgress
import com.oiid.core.designsystem.diagonalCornerShape
import com.oiid.feature.feed.list.FeedList
import com.oiid.feature.feed.list.FeedListItem
import com.oiid.feature.feed.list.FeedListItemUiState
import com.oiid.feature.feed.list.FeedUiState
import com.oiid.feature.feed.list.NavToPost
import oiid.core.base.designsystem.AppStateViewModel
import oiid.core.base.designsystem.theme.OiidTheme.colorScheme
import oiid.core.base.designsystem.theme.OiidTheme.spacing
import oiid.core.base.designsystem.theme.OiidTheme.typography
import oiid.core.ui.FeedIntent
import oiid.core.ui.appBarHeight
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
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(selectedItem?.item?.id) {
        selectedItem?.let { selected ->
            if (!selected.hasNavigated) {
                onHandleIntent(FeedIntent.ItemSelected(selected.item))
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
                    onHandleIntent(FeedIntent.ItemSelected(null))
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
                    Column(Modifier.padding(paddingValues).fillMaxSize()) {
                        Column(Modifier.heightIn(max = appBarHeight())) {
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
                                    Column(modifier = Modifier.padding(spacing.md)) {
                                        InfoTextPanel(
                                            message = error,
                                            buttons = {
                                                Button(
                                                    onClick = {
                                                        onHandleIntent(FeedIntent.RetryLoad)
                                                    },
                                                    colors = ButtonDefaults.textButtonColors().copy(
                                                        containerColor = colorScheme.surfaceVariant,
                                                        contentColor = colorScheme.onSurface,
                                                    ),
                                                    content = {
                                                        Text("Retry", style = typography.bodyLarge)
                                                    },
                                                )
                                            },
                                        )
                                    }
                                }
                            }

                            AnimatedVisibility(
                                visible = showContent,
                                enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                                exit = fadeOut(),
                            ) {
                                FeedList(
                                    modifier = Modifier.background(backgroundColor),
                                    uiState = uiState,
                                    onHandleIntent = onHandleIntent,
                                    divider = divider,
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@AnimatedContent,
                                )
                            }
                        }
                    }
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
                            onHandleIntent = onHandleIntent,
                        )
                    }
                }
            }
        }

    }
}
