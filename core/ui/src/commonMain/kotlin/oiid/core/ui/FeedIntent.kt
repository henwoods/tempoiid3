package oiid.core.ui

import com.oiid.core.model.PostItem

sealed interface FeedIntent {
    data class LikePost(val postId: String) : FeedIntent
    data class SavePost(val title: String, val content: String) : FeedIntent
    data class EditPost(val postId: String) : FeedIntent
    data class ReportPost(val postId: String) : FeedIntent

    data class ItemSelected(val item: PostItem?) : FeedIntent
    data class ErrorOccurred(val message: String) : FeedIntent
    data object RetryLoad : FeedIntent
}

fun FeedIntent.toFeedIntent(): PostIntent? {
    val feedIntent = this
    return when (feedIntent) {
        is FeedIntent.ErrorOccurred -> PostIntent.ErrorOccurred(message)
        is FeedIntent.LikePost -> PostIntent.LikePost(postId)
        is FeedIntent.ReportPost -> PostIntent.ReportPost(postId)
        is FeedIntent.RetryLoad -> PostIntent.RetryLoad
        is FeedIntent.EditPost -> PostIntent.EditPost(postId)
        is FeedIntent.ItemSelected -> null
        is FeedIntent.SavePost -> null
    }
}
