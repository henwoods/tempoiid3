package com.oiid.feature.fanzone.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.oiid.core.data.profile.ProfileRepository
import com.oiid.core.model.PostItem
import com.oiid.core.model.Profile
import com.oiid.core.model.api.Resource
import com.oiid.core.model.ui.UiEvent
import com.oiid.feature.fanzone.data.impl.FanzoneServiceImpl
import com.oiid.feature.feed.detail.PostCommentUiState
import com.oiid.feature.feed.list.FeedUiState
import com.oiid.feature.feed.list.NavToPost
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import oiid.core.base.datastore.exceptions.InvalidKeyException
import oiid.core.ui.FeedIntent

class FanzoneFeedViewModel(
    private val fanzoneService: FanzoneServiceImpl,
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private val _selectedItem = MutableStateFlow<NavToPost?>(null)
    val selectedItem: StateFlow<NavToPost?> = _selectedItem.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent

    private val _postingCommentState = MutableStateFlow(PostCommentUiState())
    val postingCommentState: StateFlow<PostCommentUiState> = _postingCommentState.asStateFlow()

    private val _showCreatePostDialog = MutableStateFlow(false)
    val showCreatePostDialog: StateFlow<Boolean> = _showCreatePostDialog.asStateFlow()

    private val _editingPost = MutableStateFlow<PostItem?>(null)
    val editingPost: StateFlow<PostItem?> = _editingPost.asStateFlow()
    
    private val _showMissingNameDialog = MutableStateFlow(false)
    val showMissingNameDialog: StateFlow<Boolean> = _showMissingNameDialog.asStateFlow()
    
    val currentUserProfile: StateFlow<Profile?> = profileRepository.currentUserProfile

    val uiState: StateFlow<FeedUiState> = fanzoneService.getPosts().map { resource ->
        val showDialog = _showCreatePostDialog.value

        when (resource) {
            is Resource.Loading -> FeedUiState(isLoading = true, showCreatePostDialog = showDialog, isForum = true)
            is Resource.Success -> FeedUiState(posts = resource.data, showCreatePostDialog = showDialog, isForum = true)
            is Resource.Error -> FeedUiState(
                error = if (resource.exception is InvalidKeyException) {
                    "User was signed out."
                } else {
                    "Failed to load forum posts."
                },
                showCreatePostDialog = showDialog,
                isForum = true,
            )
        }
    }.distinctUntilChanged().catch { emit(FeedUiState(error = "Failed to load forum posts.", isForum = true)) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FeedUiState(isLoading = true, isForum = true),
    )

    init {
        viewModelScope.launch {
            fanzoneService.loadPosts()
        }
    }

    fun handleIntent(intent: FeedIntent) {
        when (intent) {
            is FeedIntent.ErrorOccurred -> snackbar(intent.message)
            is FeedIntent.LikePost -> likePost(intent.postId)
            is FeedIntent.ItemSelected -> onPostSelected(intent.item)
            is FeedIntent.RetryLoad -> retryLoad()
            is FeedIntent.SavePost -> createPost(intent.title, intent.content)
            is FeedIntent.EditPost -> editPost(intent.postId)
            is FeedIntent.ReportPost -> reportPost(intent.postId)
        }
    }

    fun showCreatePostDialog() {
        val profile = currentUserProfile.value
        if (profile?.name.isNullOrBlank()) {
            _showMissingNameDialog.value = true
        } else {
            _showCreatePostDialog.value = true
        }
    }

    fun hideCreatePostDialog() {
        _showCreatePostDialog.value = false
        _editingPost.value = null
    }
    
    fun hideMissingNameDialog() {
        _showMissingNameDialog.value = false
    }

    private fun createPost(title: String, content: String) = viewModelScope.launch {
        _postingCommentState.value = PostCommentUiState(true)

        try {
            val editingPost = _editingPost.value
            val result = if (editingPost != null) {
                fanzoneService.updatePost(editingPost.id, title, content)
            } else {
                fanzoneService.createPost(title, content)
            }

            if (result != null) {
                hideCreatePostDialog()
                val message = if (editingPost != null) "Post updated successfully!" else "Post created successfully!"
                snackbar(message)
            } else {
                val message = if (editingPost != null) "Failed to update post." else "Failed to create post."
                snackbar(message)
            }
        } catch (e: Exception) {
            val message = if (_editingPost.value != null) "Failed to update post." else "Failed to create post."
            snackbar(message, true, e)
        }

        _postingCommentState.value = PostCommentUiState(false)
    }

    private fun likePost(postId: String) {
        viewModelScope.launch {
            try {
                val success = fanzoneService.toggleLikePost(postId)
                if (!success) {
                    snackbar("Failed to like post.")
                }
            } catch (e: Exception) {
                snackbar("Failed to like post.", true, e)
            }
        }
    }

    fun onPostSelected(post: PostItem?) {
        viewModelScope.launch {
            if (post != null) {
                _selectedItem.value = NavToPost(post, false)
            } else {
                _selectedItem.value = null
            }
        }
    }

    private fun retryLoad() {
        viewModelScope.launch {
            try {
                fanzoneService.loadPosts()
            } catch (e: Exception) {
                snackbar("Failed to load forum posts.", true, e)
            }
        }
    }

    fun editPost(postId: String) {
        val post = fanzoneService.getPostFromCache(postId)
        if (post != null) {
            _editingPost.value = post
            showCreatePostDialog()
        } else {
            snackbar("Post not found.")
        }
    }

    fun reportPost(id: String) = viewModelScope.launch {
        fanzoneService.reportPost(id)
    }

    fun setHasNavigated(hasNavigated: Boolean) {
        _selectedItem.update { it?.copy(hasNavigated = hasNavigated) }
    }

    private fun snackbar(message: String, log: Boolean = false, e: Exception? = null) = viewModelScope.launch {
        if (log) {
            if (e != null) {
                Logger.e("FanzoneViewModel", e) { message }
            } else {
                Logger.d("FanzoneViewModel") { message }
            }
        }
        _uiEvent.emit(UiEvent.ShowSnackbar(message))
    }
}
