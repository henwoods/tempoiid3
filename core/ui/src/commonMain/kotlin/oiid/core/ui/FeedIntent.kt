package oiid.core.ui

import com.oiid.core.designsystem.generated.resources.Res
import com.oiid.core.designsystem.generated.resources.dialog_generic_confirm_message
import com.oiid.core.designsystem.generated.resources.dialog_generic_confirm_title
import com.oiid.core.designsystem.generated.resources.dialog_report_confirm_button
import com.oiid.core.designsystem.generated.resources.dialog_report_dismiss_button
import com.oiid.core.designsystem.generated.resources.dialog_report_post_message
import com.oiid.core.designsystem.generated.resources.dialog_report_post_title
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

fun FeedIntent.needsConfirmation(): Boolean {
    return when (this) {
        is FeedIntent.ReportPost -> true
        else -> false
    }
}

fun FeedIntent.getDialogContent(): DialogContent {
    return when (this) {
        is FeedIntent.ReportPost -> DialogContent(
            title = Res.string.dialog_report_post_title,
            message = Res.string.dialog_report_post_message,
            confirmButtonText = Res.string.dialog_report_confirm_button,
            dismissButtonText = Res.string.dialog_report_dismiss_button
        )
        else -> DialogContent(
            title = Res.string.dialog_generic_confirm_title,
            message = Res.string.dialog_generic_confirm_message
        )
    }
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
