package com.oiid.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oiid.core.config.onboarding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OnboardingPage(val title: String, val description: String, val image: String)

data class OnboardingUiState(val pages: List<OnboardingPage> = emptyList())

class OnboardingViewmodel() : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = OnboardingUiState(
                onboarding().welcomeScreens.map {
                    OnboardingPage(
                        title = it.title,
                        description = it.body,
                        image = it.image,
                    )
                },
            )
        }
    }
}
