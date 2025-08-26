package com.oiid.feature.feed.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import co.touchlab.kermit.Logger
import com.oiid.core.designsystem.composable.DashedVerticalDivider
import com.oiid.core.designsystem.composable.LinearProgress
import com.oiid.core.designsystem.diagonalCornerShape
import com.oiid.core.designsystem.ext.bottomNavPadding
import com.oiid.core.model.PostComment
import com.oiid.core.model.PostItem
import com.oiid.feature.feed.comment.CommentInputField
import com.oiid.feature.feed.comment.CommentItem
import com.oiid.feature.feed.composables.PostCard
import com.oiid.feature.feed.list.FeedContent
import com.oiid.feature.feed.list.FeedIntent
import com.oiid.feature.feed.list.FeedItemActions
import com.oiid.feature.feed.list.FeedListItemUiState
import com.oiid.feature.feed.list.PostIntent
import com.oiid.feature.feed.list.PostIntent.ErrorOccurred
import com.oiid.feature.feed.list.PostIntent.LikePost
import com.oiid.feature.feed.list.PostIntent.ReportPost
import kotlinx.coroutines.launch
import oiid.core.base.designsystem.theme.OiidTheme
import oiid.core.base.designsystem.theme.OiidTheme.colorScheme
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PostDetailScreen(
    modifier: Modifier = Modifier,
    postId: String,
    artistId: String,
    onBackClick: () -> Unit,
    viewModel: FeedDetailViewModel = koinViewModel(key = "${artistId}_$postId") {
        parametersOf(postId, artistId)
    },
) {
    val postCommentState = viewModel.postingCommentState.collectAsState()
    val post = viewModel.post.collectAsState(PostDetailUiState())

    BaseFeedPostDetailScreen(
        modifier = modifier,
        networkStatus = postCommentState.value,
        postUiState = post.value,
        title = "Post",
        isForum = false,
        onHandleIntent = { viewModel.handleIntent(it) },
        onBackClick = onBackClick,
    )
}

@Composable
fun PostItemCard(
    post: PostItem,
    isForum: Boolean,
    onHandleIntent: (FeedIntent) -> Unit,
) {
    PostCard(
        onCardClick = {},
        shape = diagonalCornerShape(),
        content = {
            Column {
                FeedContent(
                    modifier = Modifier,
                    uiState = FeedListItemUiState(post, isPlaying = true, isForum = isForum, isDetail = true),
                    onHandleIntent = onHandleIntent,
                    maxLines = Int.MAX_VALUE,
                )
                FeedItemActions(
                    entity = post,
                    onHandleIntent = onHandleIntent,
                    onCommentClicked = {},
                )
            }
        },
    )
}

@Composable
fun PostDetailContent(
    modifier: Modifier = Modifier,
    post: PostItem,
    comments: List<PostComment>,
    isForum: Boolean,
    isLoading: Boolean = false,
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
            visibleComments.add(comment)
        }
    }

    val spaceBetweenComments = OiidTheme.spacing.sm
    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize().imePadding().background(colorScheme.background),
        contentPadding = bottomNavPadding(),
    ) {
        item {
            PostItemCard(
                post = post,
                isForum = isForum,
                onHandleIntent = {
                    Logger.d("WTF intent  1 $it")
                    when (it) {

                        is FeedIntent.ErrorOccurred -> {
                            onHandleIntent(ErrorOccurred(it.message))
                        }

                        is FeedIntent.ItemSelected -> {
                            //   onHandleIntent(PostIntent.ItemSelected(it.postId))
                        }

                        is FeedIntent.LikePost ->
                            onHandleIntent(LikePost(it.postId))

                        is FeedIntent.ReportPost -> onHandleIntent(ReportPost(it.postId))
                        is FeedIntent.RetryLoad -> onHandleIntent(PostIntent.RetryLoad)
                        is FeedIntent.SavePost -> {}//onHandleIntent(PostIntent.SavePost(it.postId))
                        is FeedIntent.EditPost -> onHandleIntent(PostIntent.EditPost(it.postId))
                    }
                },
            )
            if (!isLoading) {
                Spacer(modifier = Modifier.height(spaceBetweenComments))
            }
        }

        if (isLoading) {
            item {
                LinearProgress()
                Spacer(modifier = Modifier.height(spaceBetweenComments))
            }
        }

        if (!isForum) {
            item {
                CommentInputField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    onSendClick = {
                        if (parentComment != null) {
                            onHandleIntent(PostIntent.ReplyToComment(parentComment.commentId, commentText))
                        } else {
                            onHandleIntent(PostIntent.CommentOnPost(post.id, commentText))
                        }
                        commentText = ""
                        replyingToComment = null
                    },
                    focusRequester = focusRequester,
                    label = if (parentComment != null) replyingName else "Add a comment",
                    userAvatarImageUrl = post.profileImage,
                    onCancel = {
                        commentText = ""
                        focusRequester.freeFocus()
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
                Column(
                    modifier = Modifier.fillMaxWidth().height(spaceBetweenComments),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    DashedVerticalDivider()
                }
            }

            AnimatedVisibility(
                visible = visibleComments.contains(comment),
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300)),
            ) {
                CommentItem(
                    comment = comment,
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
        }

        if (isForum) {
            item {
                CommentInputField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    onSendClick = {
                        if (parentComment != null) {
                            onHandleIntent(PostIntent.ReplyToComment(parentComment.commentId, commentText))
                        } else {
                            onHandleIntent(PostIntent.CommentOnPost(post.id, commentText))
                        }
                        commentText = ""
                        replyingToComment = null
                    },
                    focusRequester = focusRequester,
                    label = if (parentComment != null) replyingName else "Reply to post",
                    userAvatarImageUrl = post.profileImage,
                    onCancel = {
                        commentText = ""
                        focusRequester.freeFocus()
                    },
                )
                Spacer(modifier = Modifier.height(spaceBetweenComments))
            }
        }
    }
}
