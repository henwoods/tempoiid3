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
    val forceLightTheme = false
    val useDarkTheme = true

    OiidTheme(
        oiidColorScheme = oiidTheme(),
        darkTheme = uiState.shouldUseDarkTheme(useDarkTheme),
        androidTheme = uiState.shouldUseAndroidTheme,
        useDynamicColor = uiState.shouldDisplayDynamicTheming,
    ) {
        App(
            networkMonitor = networkMonitor,
            modifier = modifier,
            isDark = useDarkTheme,
        )
    }
}
