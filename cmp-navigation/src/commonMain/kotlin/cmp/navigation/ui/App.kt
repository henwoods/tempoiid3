package cmp.navigation.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import cmp.navigation.generated.resources.Res
import cmp.navigation.generated.resources.not_connected
import cmp.navigation.navigation.FeatureNavHost
import cmp.navigation.utils.rememberDestinations
import coil3.compose.LocalPlatformContext
import com.oiid.core.LocalSnackbarHostState
import com.oiid.core.data.ui.TabRepository
import com.oiid.core.data.utils.NetworkMonitor
import com.oiid.core.designsystem.composable.FeedProgress
import com.oiid.core.designsystem.composable.ScreenBackground
import com.oiid.core.model.ui.TabItem
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.CupertinoMaterials
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import oiid.core.base.designsystem.AppStateViewModel
import oiid.core.base.designsystem.FullScreenBackground
import oiid.core.base.ui.rememberImageLoader
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

const val FEED_NAV_ANIMATION_MS = 300

class TabViewModel(tabRepository: TabRepository) : ViewModel() {
    private val _tabs = MutableStateFlow<List<TabItem>>(emptyList())
    val tabs = _tabs.asStateFlow()

    init {
        viewModelScope.launch {
            _tabs.value = tabRepository.getTabItems()
        }
    }
}

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
internal fun App(
    modifier: Modifier = Modifier,
    networkMonitor: NetworkMonitor,
    isDarkTheme: Boolean,
    appStateViewModel: AppStateViewModel = koinViewModel(),
    tabViewModel: TabViewModel = koinViewModel(),
) {
    val appState = rememberAppState(networkMonitor = networkMonitor)

    val tabs = tabViewModel.tabs.collectAsState().value
    val destination = appState.currentTopLevelDestination
    val destinationInfo = rememberDestinations(
        allDestinations = appState.topLevelDestinations,
        availableTabs = tabs,
    )

    val isOffline by appState.isOffline.collectAsStateWithLifecycle()

    val notConnectedMessage = stringResource(Res.string.not_connected)
    var snackbarJob by remember { mutableStateOf<Job?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(isOffline, snackbarHostState) {
        if (isOffline) {
            snackbarJob?.cancel()
            snackbarJob = launch {
                snackbarHostState.showSnackbar(
                    message = notConnectedMessage,
                    duration = SnackbarDuration.Indefinite,
                )
            }
        } else {
            snackbarJob?.cancel()
            snackbarJob = null
        }
    }

    val background by appStateViewModel.background.collectAsState()
    val isBlurring by appStateViewModel.foregroundBlur.collectAsState()
    val immutableBlurInfo = isBlurring

    val imageLoader = rememberImageLoader(LocalPlatformContext.current)
    val hazeState = rememberHazeState()

    val targetColor = if (background.background == FullScreenBackground.None) {
        MaterialTheme.colorScheme.background
    } else {
        Color.Transparent
    }

    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = FEED_NAV_ANIMATION_MS),
        label = "AppBackgroundColorFade",
    )

    val appBarLambda = remember(destination) {
        @Composable {
            ArtistAppBar(
                isDark = isDarkTheme,
                destination = destination,
                destinationInfo = destinationInfo,
                appState = appState,
            )
        }
    }

    Box(Modifier.fillMaxSize()) {
        ScreenBackground(
            imageLoader = imageLoader,
            background = background.background,
            modifier = modifier.fillMaxSize(),
        )

        CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
            Scaffold(
                modifier = modifier.hazeSource(hazeState),
                containerColor = animatedColor,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                bottomBar = {
                    if (appState.shouldShowBottomBar && destination != null) {
                        BottomBar(
                            destinations = destinationInfo.destinations,
                            destinationsWithUnreadResources = emptySet(),
                            onNavigateToDestination = appState::navigateToTopLevelDestination,
                            currentDestination = appState.currentDestination,
                            modifier = modifier.testTag("NiaBottomBar").navigationBarsPadding(),
                        )
                    }
                },
            ) { padding ->
                Row(
                    modifier.fillMaxSize().padding(
                        top = padding.calculateTopPadding(),
                        start = padding.calculateStartPadding(LayoutDirection.Ltr),
                        end = padding.calculateEndPadding(LayoutDirection.Ltr),
                    ).consumeWindowInsets(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
                    ).windowInsetsPadding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal),
                    ),
                ) {
                    FeatureNavHost(
                        navController = appState.navController,
                        appBar = appBarLambda,
                        inDarkTheme = isDarkTheme,
                    )
                }
            }
        }

        if (immutableBlurInfo != null) {
            Column(
                modifier = Modifier.hazeEffect(
                    state = hazeState,
                    style = CupertinoMaterials.ultraThin(),
                ).fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedContent(
                    targetState = immutableBlurInfo.title,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "FeedProgressTextAnimation",
                ) { animatedTitle ->
                    FeedProgress(animatedTitle)
                }
            }
        }
    }
}
