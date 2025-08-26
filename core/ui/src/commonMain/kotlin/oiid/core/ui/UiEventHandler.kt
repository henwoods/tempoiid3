package oiid.core.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.oiid.core.LocalSnackbarHostState
import com.oiid.core.model.ui.UiEvent
import kotlinx.coroutines.flow.Flow
import oiid.core.base.platform.LocalIntentManager

@Composable
fun UiEventHandler(uiEventFlow: Flow<UiEvent>) {
    val intentManager = LocalIntentManager.current
    val snackbarHostState = LocalSnackbarHostState.current

    LaunchedEffect(Unit) {
        uiEventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                is UiEvent.LaunchUri -> {
                    try {
                        intentManager.launchUri(event.uri)
                    } catch (e: Exception) {
                        snackbarHostState.showSnackbar("Could not open link: ${e.message}")
                    }
                }
            }
        }
    }
}