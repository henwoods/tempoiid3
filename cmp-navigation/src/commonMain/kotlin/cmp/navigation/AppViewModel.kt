package cmp.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.oiid.core.data.user.UserRepository
import com.oiid.core.datastore.UserPreferencesRepository
import com.oiid.core.model.DarkThemeConfig
import com.oiid.core.model.ThemeBrand
import com.oiid.core.model.UserData
import com.oiid.core.model.api.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    private val settingsRepository: UserPreferencesRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    val uiState: StateFlow<AppUiState> = settingsRepository.userData.map { userDate ->
        AppUiState.Success(userDate)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AppUiState.Loading,
    )

    init {
        viewModelScope.launch {
            ensureUserId()
        }
    }

    private suspend fun ensureUserId(): String {
        val cached = userRepository.getUserId()
        Logger.d("AppViewModel ensureUserId - cached: $cached")
        if (!cached.isNullOrBlank()) {
            settingsRepository.setUserId(cached)
            return cached
        }

        return when (val result = userRepository.fetchAndStoreUserInfo()) {
            is Resource.Success -> {
                val userId = result.data.userId
                Logger.d("AppViewModel ensureUserId - fetched userId: $userId")
                settingsRepository.setUserId(userId)
                userId
            }

            else -> {
                Logger.w("AppViewModel ensureUserId - failed to fetch userId")
                ""
            }
        }
    }

    fun onScreenChanged(title: String) {
        _title.value = title
    }
}

sealed interface AppUiState {
    data object Loading : AppUiState
    data class Success(val userData: UserData) : AppUiState {
        override val shouldDisplayDynamicTheming = userData.useDynamicColor
        override val shouldUseAndroidTheme = when (userData.themeBrand) {
            ThemeBrand.DEFAULT -> false
            ThemeBrand.ANDROID -> true
        }

        override fun shouldUseDarkTheme(isSystemInDarkTheme: Boolean): Boolean = when (userData.darkThemeConfig) {
            DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme
            DarkThemeConfig.LIGHT -> false
            DarkThemeConfig.DARK -> true
        }
    }

    val shouldDisplayDynamicTheming: Boolean get() = true
    val shouldUseAndroidTheme: Boolean get() = false
    fun shouldUseDarkTheme(isSystemInDarkTheme: Boolean) = isSystemInDarkTheme
}
