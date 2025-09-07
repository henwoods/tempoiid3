package cmp.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmp.navigation.ui.App
import com.oiid.core.config.oiidTheme
import com.oiid.core.data.utils.NetworkMonitor
import com.oiid.core.designsystem.theme.OiidTheme
import oiid.core.base.designsystem.theme.SystemBarsEffect
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ComposeApp(
    modifier: Modifier = Modifier,
    networkMonitor: NetworkMonitor = koinInject(),
    viewModel: AppViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState is AppUiState.Loading) {
        return
    }

    val isSystemInDarkTheme = uiState.shouldUseDarkTheme(isSystemInDarkTheme())

    SystemBarsEffect(isSystemInDarkTheme)

    OiidTheme(
        oiidColorScheme = oiidTheme(),
        darkTheme = uiState.shouldUseDarkTheme(isSystemInDarkTheme),
        androidTheme = uiState.shouldUseAndroidTheme,
        useDynamicColor = uiState.shouldDisplayDynamicTheming,
    ) {
        App(
            networkMonitor = networkMonitor,
            modifier = modifier,
            isDarkTheme = isSystemInDarkTheme,
        )
    }
}