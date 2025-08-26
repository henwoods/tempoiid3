package com.oiid.feature.feed.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.oiid.core.datastore.PostService
import com.oiid.core.model.PostComment
import com.oiid.core.model.PostItem
import com.oiid.core.model.api.Resource
import com.oiid.core.model.ui.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import oiid.core.ui.PostIntent
import org.koin.core.component.KoinComponent

data class PostDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val postItem: PostItem? = null,
    val comments: List<PostComment> = emptyList(),
    val isForum: Boolean,
)

data class PostCommentUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
)

open class FeedPostDetailViewModel(
    private val postId: String,
    private val artistId: String,
    private val postService: PostService,
    private val isForum: Boolean = false,
) : ViewModel(), KoinComponent {

    private val _postingCommentState = MutableStateFlow(PostCommentUiState())
    val postingCommentState: StateFlow<PostCommentUiState> = _postingCommentState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent

    private val _editingPost = MutableStateFlow<PostItem?>(null)
    val editingPost: StateFlow<PostItem?> = _editingPost.asStateFlow()

    private val _showCreatePostDialog = MutableStateFlow(false)
    val showCreatePostDialog: StateFlow<Boolean> = _showCreatePostDialog.asStateFlow()

    val post: StateFlow<PostDetailUiState> = postService.getPost().map { resource ->
        when (resource) {
            is Resource.Loading -> PostDetailUiState(isLoading = true, isForum = isForum)
            is Resource.Error -> PostDetailUiState(error = resource.exception.message, isForum = isForum)
            is Resource.Success -> {
                val newPost = resource.data.postItem
                val sortedComments = sortComments(resource.data.comments)
                PostDetailUiState(
                    postItem = newPost,
                    comments = sortedComments,
                    isLoading = resource.isLoading,
                    isForum = isForum,
                )
            }
        }
    }.distinctUntilChanged().stateIn(viewModelScope, SharingStarted.Eagerly, PostDetailUiState(isForum = isForum))

    fun sortComments(allComments: List<PostComment>, newestFirst: Boolean = true): List<PostComment> {
        val topLevelComments = allComments.filter { it.parentCommentId == null }
        val replies = allComments.filter { it.parentCommentId != null }

        val repliesByParentId = replies.groupBy { it.parentCommentId }

        fun buildSortedHierarchy(
            parentId: String?,
            currentLevelComments: List<PostComment>,
        ): List<PostComment> {
            val sortedCurrentLevel = if (newestFirst) {
                currentLevelComments.sortedByDescending { it.createdAt }
            } else {
                currentLevelComments.sortedBy { it.createdAt }
            }

            val result = mutableListOf<PostComment>()
            for (comment in sortedCurrentLevel) {
                result.add(comment)
                val directReplies = repliesByParentId[comment.commentId] ?: emptyList()
                if (directReplies.isNotEmpty()) {
                    result.addAll(buildSortedHierarchy(comment.commentId, directReplies))
                }
            }
            return result
        }

        return buildSortedHierarchy(null, topLevelComments)
    }

    fun onLikeCommentClicked(commentId: String) {
        viewModelScope.launch {
            postService.toggleLikeComment(artistId, postId, commentId)
        }
    }

    fun onLikeClicked() {
        viewModelScope.launch {
            postService.likePost(artistId, postId)
        }
    }

    fun onComment(content: String) {
        viewModelScope.launch {
            try {
                _postingCommentState.update {
                    PostCommentUiState(true)
                }
                val comment = postService.createComment(
                    artistId = artistId,
                    postId = postId,
                    content = content,
                )
                _postingCommentState.update {
                    PostCommentUiState(false)
                }
                snackbar("Comment posted successfully!")
            } catch (e: Exception) {
                _postingCommentState.update {
                    PostCommentUiState(false, e.message)
                }
            }
        }
    }

    fun onReply(parentCommentId: String, content: String) {
        viewModelScope.launch {
            try {
                _postingCommentState.update {
                    PostCommentUiState(true)
                }
                val newComment = postService.postCommentReply(artistId, postId, parentCommentId, content)

                newComment.commentId
                _postingCommentState.update {
                    PostCommentUiState(false)
                }
                snackbar("Reply posted successfully!")
            } catch (e: Exception) {
                _postingCommentState.update {
                    PostCommentUiState(false, e.message)
                }
            }
        }
    }

    fun onReport(intent: PostIntent) {
        viewModelScope.launch {
            try {
                _postingCommentState.update {
                    PostCommentUiState(true)
                }
                val success = if (intent is PostIntent.ReportPost) {
                    postService.reportPost(artistId, postId)
                } else if (intent is PostIntent.ReportComment) {
                    postService.reportComment(artistId, postId, intent.commentId)
                } else {
                    false
                }
                _postingCommentState.update {
                    if (success) {
                        PostCommentUiState(false)
                    } else {
                        PostCommentUiState(false, "Failed to report comment")
                    }
                }
            } catch (e: Exception) {
                _postingCommentState.update {
                    PostCommentUiState(false, e.message)
                }
            }
        }
    }

    fun updatePost(title: String, content: String) = viewModelScope.launch {
        val editingPost = _editingPost.value
        if (editingPost != null) {
            try {
                _postingCommentState.update { PostCommentUiState(true) }

                val updatedPost = postService.updatePost(artistId, editingPost.id, title, content)

                if (updatedPost != null) {
                    hideCreatePostDialog()
                    Logger.d("Post updated successfully: ${editingPost.id}")
                } else {
                    Logger.e("Failed to update post: ${editingPost.id}")
                }

                _postingCommentState.update { PostCommentUiState(false) }
            } catch (e: Exception) {
                Logger.e("Error updating post: ${e.message}")
                _postingCommentState.update { PostCommentUiState(false, e.message) }
            }
        }
    }

    fun showCreatePostDialog() {
        _showCreatePostDialog.value = true
    }

    fun hideCreatePostDialog() {
        _showCreatePostDialog.value = false
        _editingPost.value = null
    }

    private fun editPost(postId: String) {
        val post = postService.getPostFromCache(postId)
        if (post != null) {
            _editingPost.value = post
            showCreatePostDialog()
        } else {
            snackbar("Could not edit post!")
            Logger.w("Post not found in cache: $postId")
        }
    }

    private fun snackbar(message: String) = viewModelScope.launch {
        _uiEvent.emit(UiEvent.ShowSnackbar(message))
    }


    fun handleIntent(intent: PostIntent) {
        when (intent) {
            is PostIntent.ReplyToPost -> onComment(content = intent.content)
            is PostIntent.ReportComment, is PostIntent.ReportPost -> onReport(intent)
            is PostIntent.LikePost -> onLikeClicked()
            is PostIntent.ReplyToComment -> onReply(parentCommentId = intent.commentId, content = intent.content)
            is PostIntent.RetryLoad -> viewModelScope.launch { postService.loadPost() }
            is PostIntent.LikeComment -> onLikeCommentClicked(intent.commentId)
            is PostIntent.ErrorOccurred -> snackbar(intent.message)
            is PostIntent.EditPost -> editPost(intent.postId)
        }
    }
}
