package com.oiid.feature.feed.list

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.oiid.core.config.artistId
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

data class NavToPost(val item: PostItem, val hasNavigated: Boolean)

sealed interface FeedIntent {
    data class SavePost(val title: String, val content: String) : FeedIntent
    data class ReportPost(val postId: String) : FeedIntent
    data class LikePost(val postId: String) : FeedIntent
    data class EditPost(val postId: String) : FeedIntent
    data class ItemSelected(val item: PostItem?) : FeedIntent
    data class ErrorOccurred(val message: String) : FeedIntent
    data object RetryLoad : FeedIntent
}

sealed interface PostIntent {
    data class LikePost(val postId: String) : PostIntent
    data class LikeComment(val commentId: String) : PostIntent
    data class ReportPost(val postId: String) : PostIntent
    data class ReportComment(val commentId: String) : PostIntent
    data class EditPost(val postId: String) : PostIntent
    data class CommentOnPost(val postId: String, val content: String) : PostIntent
    data class ReplyToComment(val commentId: String, val content: String) : PostIntent
    data class ErrorOccurred(val message: String) : PostIntent
    data object RetryLoad : PostIntent
}

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
                is Resource.Loading -> FeedUiState(isLoading = true, isForum =  false)
                is Resource.Success -> FeedUiState(posts = resource.data, isLoading = false, isForum =  false)
                is Resource.Error -> FeedUiState(
                    error = if (resource.exception is InvalidKeyException) {
                        "User was signed out."
                    } else {
                        "Failed to load feed."
                    }, isForum =  false
                )
            }
        }
        .distinctUntilChanged()
        .catch { emit(FeedUiState(error = "Failed to load feed.", isForum =  false)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FeedUiState(isLoading = true, isForum =  false),
        )

    val isOnboardingCompleted = userPreferencesRepository.observeOnboardingComplete()

    init {
        // Feed service might already have been initialized when the user was not authenticated
        // so need to attempt to load again.
        viewModelScope.launch {
            feedService.loadPosts()
        }
    }

    fun onLikeClicked(postId: String) {
        launchCatching {
            val success = feedService.toggleLikePost(postId)
            if (!success) {
                _uiEvent.emit(UiEvent.ShowSnackbar("Liking post failed."))
            }
        }
    }

    fun setOnboardingComplete() {
        viewModelScope.launch {
            userPreferencesRepository.onboardingComplete()
        }
    }

    fun onFeedItemSelected(item: PostItem?) {
        viewModelScope.launch {
            if (item != null) {
                _selectedItem.value = NavToPost(item, false)
            } else {
                _selectedItem.value = null
            }
        }
    }

    fun setHasNavigated(hasNavigated: Boolean) {
        _selectedItem.update { it?.copy(hasNavigated = hasNavigated) }
    }

    fun retryLoad() {
        launchCatching {
            feedService.loadPosts()
        }
    }

    private fun showSnackbar(message: String) = viewModelScope.launch {
        _uiEvent.emit(UiEvent.ShowSnackbar(message))
    }

    fun handleIntent(intent: FeedIntent) {
        when (intent) {
            is FeedIntent.ErrorOccurred -> showSnackbar(message = intent.message)

            is FeedIntent.LikePost -> {
                onLikeClicked(intent.postId)
            }

            is FeedIntent.RetryLoad -> {
                retryLoad()
            }

            is FeedIntent.ItemSelected -> {
                onFeedItemSelected(intent.item)
            }

            is FeedIntent.SavePost -> {
                viewModelScope.launch {
                    feedService.createPost(intent.title, intent.title)
                }
            }

            is FeedIntent.ReportPost -> TODO()
            is FeedIntent.EditPost -> TODO()
        }
    }
}
