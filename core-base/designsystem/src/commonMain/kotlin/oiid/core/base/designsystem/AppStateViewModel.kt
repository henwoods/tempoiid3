package oiid.core.base.designsystem

import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed interface FullScreenBackground {
    data class Paint(val painter: Painter) : FullScreenBackground
    data class Resource(val res: String) : FullScreenBackground
    data class Url(val url: String) : FullScreenBackground
    data object None : FullScreenBackground
}

data class BlurInfo(val title: String)

data class BackgroundState(
    val key: String? = null,
    val instance: Any? = null,
    val background: FullScreenBackground = FullScreenBackground.None,
)

class AppStateViewModel : ViewModel() {
    private val _background = MutableStateFlow(BackgroundState())
    val background: StateFlow<BackgroundState> = _background

    private val _foregroundBlur = MutableStateFlow<BlurInfo?>(null)
    val foregroundBlur: StateFlow<BlurInfo?> = _foregroundBlur.asStateFlow()

    fun setForegroundBlur(text: String? = null) {
        if (text != null) {
            _foregroundBlur.update { BlurInfo(text) }
        } else {
            _foregroundBlur.update { null }
        }
    }

    fun setBackground(background: FullScreenBackground, key: String, instance: Any) {
        _background.value = BackgroundState(key, instance, background)
    }

    fun clearBackground(key: String, instance: Any?, force: Boolean = false) {
        _background.update { currentState ->
            if ((currentState.key == key && currentState.instance == instance) || force) {
                BackgroundState()
            } else {
                currentState
            }
        }
    }
}
