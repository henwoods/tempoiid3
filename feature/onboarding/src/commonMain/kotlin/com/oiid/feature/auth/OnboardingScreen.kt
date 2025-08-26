package com.oiid.feature.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import oiid.core.base.designsystem.theme.OiidTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    viewModel: OnboardingViewmodel = koinViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState()
    OnboardingScreenContent(
        modifier = modifier.fillMaxSize(),
        onDismiss = onDismiss,
        uiState = uiState.value,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OnboardingScreenContent(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    uiState: OnboardingUiState,
) {
    val pagerState = rememberPagerState(pageCount = { uiState.pages.size })
    val bottomSheetState = rememberModalBottomSheetState(true)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(bottomSheetState) {
        bottomSheetState.show()
    }

    ModalBottomSheet(
        modifier = modifier.padding(top = OiidTheme.spacing.xxl),
        dragHandle = {},
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
        onDismissRequest = {
            onDismiss()
        },
        sheetState = bottomSheetState,
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                HorizontalPager(modifier = Modifier.fillMaxSize(), state = pagerState) { page ->
                    OnboardingPage(page = uiState.pages[page])
                }

                PageIndicator(pagerState)

                DismissIcon {
                    coroutineScope.launch {
                        bottomSheetState.hide()
                    }

                    onDismiss()
                }
            }
        },
    )
}

@Composable
fun BoxScope.DismissIcon(onDismiss: () -> Unit) {
    IconButton(
        modifier = Modifier.align(Alignment.TopStart).padding(OiidTheme.spacing.md),
        onClick = onDismiss,
        content = {
            Icon(imageVector = Icons.Filled.Close, contentDescription = "Dismiss onboarding")
        },
    )
}
