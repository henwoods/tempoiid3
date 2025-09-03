package com.oiid.feature.feed.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import com.oiid.core.designsystem.composable.DashedVerticalDivider
import com.oiid.core.designsystem.composable.LinearProgress
import com.oiid.core.designsystem.ext.bottomNavPadding
import com.oiid.core.model.PostComment
import com.oiid.core.model.PostItem
import com.oiid.core.model.Profile
import com.oiid.feature.feed.comment.CommentInputField
import com.oiid.feature.feed.comment.CommentItem
import com.oiid.feature.feed.list.FeedListItemUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import oiid.core.base.designsystem.theme.OiidTheme
import oiid.core.base.designsystem.theme.OiidTheme.colorScheme
import oiid.core.ui.PostIntent
import oiid.core.ui.toFeedIntent

@Composable
fun PostDetailList(
    modifier: Modifier = Modifier,
    post: PostItem,
    comments: List<PostComment>,
    currentUserProfile: Profile?,
    isForum: Boolean,
    isLoading: Boolean = false,
    isPostingComment: Boolean = false,
    onHandleIntent: (PostIntent) -> Unit,
) {
    val listState = remember(post.id) { LazyListState() }
    var commentText by remember { mutableStateOf("") }

    var replyingToComment by remember { mutableStateOf<PostComment?>(null) }
    val parentComment = replyingToComment

    val replyingName = if (parentComment?.name.isNullOrEmpty()) "Replying..." else "Replying to ${parentComment.name}"
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val visibleComments = remember { mutableStateListOf<PostComment>() }

    LaunchedEffect(comments) {
        visibleComments.clear()
        comments.forEachIndexed { index, comment ->
            delay(index * 50L)
            visibleComments.add(comment)
        }
    }


    fun onCancel() {
        commentText = ""
        focusRequester.freeFocus()
    }

    fun onHandleCommentSubmission() {
        if (parentComment != null) {
            onHandleIntent(PostIntent.ReplyToComment(parentComment.commentId, commentText))
        } else {
            onHandleIntent(PostIntent.ReplyToPost(post.id, commentText))
        }
        commentText = ""
        replyingToComment = null
    }

    val spaceBetweenComments = OiidTheme.spacing.sm

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize().imePadding().background(colorScheme.background),
        contentPadding = bottomNavPadding(),
    ) {
        item(key = "post-card") {
            PostItemCard(
                uiState = FeedListItemUiState(post, false, isForum, true),
                onHandleIntent = {
                    val mappedIntent = it.toFeedIntent()
                    if (mappedIntent != null) {
                        onHandleIntent(mappedIntent)
                    }
                },
            )

            if (!isLoading) {
                Spacer(modifier = Modifier.height(spaceBetweenComments))
            }
        }

        if (isLoading) {
            item(key = "loading-indicator") {
                LinearProgress()
                Spacer(modifier = Modifier.height(spaceBetweenComments))
            }
        }

        if (!isForum) {
            item(key = "comment-input") {
                CommentInputField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    onSendClick = {
                        onHandleCommentSubmission()
                    },
                    focusRequester = focusRequester,
                    label = if (parentComment != null) replyingName else "Add a comment",
                    userAvatarImageUrl = currentUserProfile?.profileImage,
                    userName = currentUserProfile?.name,
                    enabled = !isLoading && !isPostingComment,
                    isLoading = isPostingComment,
                    onCancel = {
                        onCancel()
                    },
                )
                Spacer(modifier = Modifier.height(spaceBetweenComments))
            }
        }

        itemsIndexed(items = comments, key = { index, comment -> comment.commentId }) { index, comment ->
            if (index == 0) {
                // Don't space above the first row
            } else if (comment.parentCommentId == null) {
                Spacer(modifier = Modifier.height(spaceBetweenComments))
            } else {
                if (!isLoading) {
                    Column(
                        modifier = Modifier.fillMaxWidth().height(spaceBetweenComments),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        DashedVerticalDivider()
                    }
                } else {
                    Spacer(modifier = Modifier.height(spaceBetweenComments))
                }
            }

            CommentItem(
                modifier = Modifier.animateItem(),
                comment = comment,
                post = post,
                onStartReplying = {
                    replyingToComment = comment
                    commentText = ""
                    coroutineScope.launch {
                        kotlinx.coroutines.yield()
                        listState.animateScrollToItem(if (isForum) comments.size else 1)
                        focusRequester.requestFocus()
                    }
                },
                onHandleIntent = onHandleIntent,
            )
        }

        if (isForum) {
            item(key = "comment-input") {
                Column(Modifier.animateItem()) {
                    Spacer(modifier = Modifier.height(spaceBetweenComments))
                    CommentInputField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        onSendClick = {
                            onHandleCommentSubmission()
                        },
                        focusRequester = focusRequester,
                        label = if (parentComment != null) replyingName else "Post a reply",
                        userAvatarImageUrl = currentUserProfile?.profileImage,
                        userName = currentUserProfile?.name,
                        enabled = !isLoading && !isPostingComment,
                        isLoading = isPostingComment,
                        onCancel = {
                            commentText = ""
                            focusRequester.freeFocus()
                        },
                    )
                }
            }
        }
    }
}
