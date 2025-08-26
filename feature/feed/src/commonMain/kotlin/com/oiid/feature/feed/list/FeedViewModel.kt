package com.oiid.feature.feed.list

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.oiid.core.datastore.UserPreferencesRepository
import com.oiid.core.model.PostItem
import com.oiid.core.model.api.Resource
import com.oiid.core.model.ui.UiEvent
import com.oiid.feature.feed.LoggingViewModel
import com.oiid.feature.feed.data.impl.FeedServiceImpl
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import oiid.core.base.datastore.exceptions.InvalidKeyException
import oiid.core.ui.FeedIntent

data class NavToPost(val item: PostItem, val hasNavigated: Boolean)

class FeedViewModel(
    private val feedService: FeedServiceImpl,
    private val userPreferencesRepository: UserPreferencesRepository,
) : LoggingViewModel() {

    private val _selectedItem = MutableStateFlow<NavToPost?>(null)
    val selectedItem: StateFlow<NavToPost?> = _selectedItem.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent
    init {
        Logger.d("FeedViewModel created: ${this.hashCode()}")
    }

    @OptIn(FlowPreview::class)
    val uiState: StateFlow<FeedUiState> = feedService.getPosts()
        .map { resource ->
            when (resource) {
                is Resource.Loading -> FeedUiState(isLoading = true, isForum = false)
                is Resource.Success -> FeedUiState(posts = resource.data, isLoading = false, isForum = false)
                is Resource.Error -> FeedUiState(
                    error = if (resource.exception is InvalidKeyException) {
                        "User was signed out."
                    } else {
                        "Failed to load feed."
                    },
                    isForum = false,
                )
            }
        }
        .distinctUntilChanged()
        .catch { emit(FeedUiState(error = "Failed to load feed.", isForum = false)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FeedUiState(isLoading = true, isForum = false),
        )

    val isOnboardingCompleted = userPreferencesRepository.observeOnboardingComplete()

    init {
        // Feed service might already have been initialized when the user was not authenticated
        // so need to attempt to load again.
        viewModelScope.launch {
            feedService.loadPosts()
        }
    }

    fun setOnboardingComplete() = launchCatching {
        userPreferencesRepository.onboardingComplete()
    }

    fun setHasNavigated(hasNavigated: Boolean) {
        _selectedItem.update { it?.copy(hasNavigated = hasNavigated) }
    }

    fun onFeedItemSelected(item: PostItem?) = launchCatching {
        if (item != null) {
            _selectedItem.value = NavToPost(item, false)
        } else {
            _selectedItem.value = null
        }
    }

    fun retryLoad() = launchCatching {
        feedService.loadPosts()
    }

    fun savePost(title: String, content: String) = launchCatching {
        feedService.createPost(title, content)
    }

    fun reportPost(id: String) = launchCatching {
        feedService.reportPost(id)
    }

    fun onLikeClicked(postId: String) = launchCatching {
        val success = feedService.toggleLikePost(postId)
        if (!success) {
            snackbar("Liking post failed.")
        }
    }

    fun handleIntent(intent: FeedIntent) {
        when (intent) {
            is FeedIntent.LikePost -> onLikeClicked(intent.postId)
            is FeedIntent.SavePost -> savePost(intent.title, intent.content)
            is FeedIntent.EditPost -> {}
            is FeedIntent.ReportPost -> reportPost(intent.postId)

            is FeedIntent.ItemSelected -> onFeedItemSelected(intent.item)
            is FeedIntent.ErrorOccurred -> snackbar(intent.message)
            is FeedIntent.RetryLoad -> retryLoad()
        }
    }

    private fun snackbar(message: String) = viewModelScope.launch {
        _uiEvent.emit(UiEvent.ShowSnackbar(message))
    }
}
