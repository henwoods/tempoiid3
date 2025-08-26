package oiid.core.ui

sealed interface PostIntent {
    data class LikePost(val postId: String) : PostIntent
    data class EditPost(val postId: String) : PostIntent
    data class ReportPost(val postId: String) : PostIntent

    data class LikeComment(val commentId: String) : PostIntent
    data class ReportComment(val commentId: String) : PostIntent

    data class ReplyToPost(val postId: String, val content: String) : PostIntent
    data class ReplyToComment(val commentId: String, val content: String) : PostIntent

    data class ErrorOccurred(val message: String) : PostIntent
    data object RetryLoad : PostIntent
}

fun PostIntent.toFeedIntent(): FeedIntent? {
    return when (this) {
        is PostIntent.EditPost -> FeedIntent.EditPost(postId)
        is PostIntent.ErrorOccurred -> FeedIntent.ErrorOccurred(message)
        is PostIntent.LikePost -> FeedIntent.LikePost(postId)
        is PostIntent.ReportPost -> FeedIntent.ReportPost(postId)
        is PostIntent.RetryLoad -> FeedIntent.RetryLoad
        is PostIntent.ReplyToPost -> null
        is PostIntent.LikeComment -> null
        is PostIntent.ReplyToComment -> null
        is PostIntent.ReportComment -> null
    }
}
