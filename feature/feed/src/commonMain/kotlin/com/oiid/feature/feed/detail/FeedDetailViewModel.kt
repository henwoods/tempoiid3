package com.oiid.feature.feed.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.oiid.core.model.PostComment
import com.oiid.core.model.PostItem
import com.oiid.core.model.api.Resource
import com.oiid.core.datastore.PostService
import com.oiid.feature.feed.list.PostIntent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

data class PostDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val postItem: PostItem? = null,
    val comments: List<PostComment> = emptyList(),
)

data class PostCommentUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
)

open class FeedDetailViewModel(
    private val postId: String,
    private val artistId: String,
    private val postService: PostService,
) : ViewModel(), KoinComponent {

    private val _postingCommentState = MutableStateFlow(PostCommentUiState())
    val postingCommentState: StateFlow<PostCommentUiState> = _postingCommentState.asStateFlow()

    private val _editingPost = MutableStateFlow<PostItem?>(null)
    val editingPost: StateFlow<PostItem?> = _editingPost.asStateFlow()

    private val _showCreatePostDialog = MutableStateFlow(false)
    val showCreatePostDialog: StateFlow<Boolean> = _showCreatePostDialog.asStateFlow()

    val post: StateFlow<PostDetailUiState> = postService.getPost().map { resource ->
        when (resource) {
            is Resource.Loading -> PostDetailUiState(isLoading = true)
            is Resource.Error -> PostDetailUiState(error = resource.exception.message)
            is Resource.Success -> {
                val newPost = resource.data.postItem
                val sortedComments = sortComments(resource.data.comments)
                PostDetailUiState(postItem = newPost, comments = sortedComments, isLoading = resource.isLoading)
            }
        }
    }.distinctUntilChanged().stateIn(viewModelScope, SharingStarted.Eagerly, PostDetailUiState())

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
                postService.createComment(
                    artistId = artistId,
                    postId = postId,
                    content = content,
                )
                _postingCommentState.update {
                    PostCommentUiState(false)
                }
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
                postService.postCommentReply(artistId, postId, parentCommentId, content)
                _postingCommentState.update {
                    PostCommentUiState(false)
                }
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

    fun clearEditingPost() {
        _editingPost.value = null
    }

    fun showCreatePostDialog() {
        _showCreatePostDialog.value = true
    }

    fun hideCreatePostDialog() {
        _showCreatePostDialog.value = false
        _editingPost.value = null
    }

    fun handleIntent(intent: PostIntent) {
        when (intent) {
            is PostIntent.CommentOnPost -> onComment(content = intent.content)
            is PostIntent.ReportComment, is PostIntent.ReportPost -> onReport(intent)
            is PostIntent.LikePost -> onLikeClicked()
            is PostIntent.ReplyToComment -> onReply(parentCommentId = intent.commentId, content = intent.content)
            is PostIntent.RetryLoad -> viewModelScope.launch { postService.loadPost() }
            is PostIntent.LikeComment -> onLikeCommentClicked(intent.commentId)
            is PostIntent.ErrorOccurred -> {

            }

            is PostIntent.EditPost -> {
                val post = postService.getPostFromCache(intent.postId)
                if (post != null) {
                    _editingPost.value = post
                    showCreatePostDialog()
                } else {
                    // Post not found in cache, could emit an error or try to fetch it
                    Logger.w("Post not found in cache: ${intent.postId}")
                }
            }
        }
    }
}
