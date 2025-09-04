package oiid.core.ui

import com.oiid.core.designsystem.generated.resources.Res
import com.oiid.core.designsystem.generated.resources.dialog_generic_confirm_message
import com.oiid.core.designsystem.generated.resources.dialog_generic_confirm_title
import com.oiid.core.designsystem.generated.resources.dialog_generic_ok_button
import com.oiid.core.designsystem.generated.resources.dialog_generic_validation_message
import com.oiid.core.designsystem.generated.resources.dialog_generic_validation_title
import com.oiid.core.designsystem.generated.resources.dialog_missing_profile_name_confirm_button
import com.oiid.core.designsystem.generated.resources.dialog_missing_profile_name_dismiss_button
import com.oiid.core.designsystem.generated.resources.dialog_missing_profile_name_message
import com.oiid.core.designsystem.generated.resources.dialog_missing_profile_name_title
import com.oiid.core.designsystem.generated.resources.dialog_report_comment_message
import com.oiid.core.designsystem.generated.resources.dialog_report_comment_title
import com.oiid.core.designsystem.generated.resources.dialog_report_confirm_button
import com.oiid.core.designsystem.generated.resources.dialog_report_dismiss_button
import com.oiid.core.designsystem.generated.resources.dialog_report_post_message
import com.oiid.core.designsystem.generated.resources.dialog_report_post_title

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

fun PostIntent.needsConfirmation(): Boolean {
    return when (this) {
        is PostIntent.ReportPost, is PostIntent.ReportComment -> true
        else -> false
    }
}

fun PostIntent.needsNameValidation(): Boolean {
    return when (this) {
        is PostIntent.ReplyToPost, is PostIntent.ReplyToComment -> true
        else -> false
    }
}

fun PostIntent.getDialogContent(): DialogContent {
    return when (this) {
        is PostIntent.ReportPost -> DialogContent(
            title = Res.string.dialog_report_post_title,
            message = Res.string.dialog_report_post_message,
            confirmButtonText = Res.string.dialog_report_confirm_button,
            dismissButtonText = Res.string.dialog_report_dismiss_button
        )
        is PostIntent.ReportComment -> DialogContent(
            title = Res.string.dialog_report_comment_title,
            message = Res.string.dialog_report_comment_message,
            confirmButtonText = Res.string.dialog_report_confirm_button,
            dismissButtonText = Res.string.dialog_report_dismiss_button
        )
        else -> DialogContent(
            title = Res.string.dialog_generic_confirm_title,
            message = Res.string.dialog_generic_confirm_message
        )
    }
}

fun PostIntent.getValidationDialogContent(): DialogContent {
    return when (this) {
        is PostIntent.ReplyToPost, is PostIntent.ReplyToComment -> DialogContent(
            title = Res.string.dialog_missing_profile_name_title,
            message = Res.string.dialog_missing_profile_name_message,
            confirmButtonText = Res.string.dialog_missing_profile_name_confirm_button,
            dismissButtonText = Res.string.dialog_missing_profile_name_dismiss_button
        )
        else -> DialogContent(
            title = Res.string.dialog_generic_validation_title,
            message = Res.string.dialog_generic_validation_message,
            confirmButtonText = Res.string.dialog_generic_ok_button
        )
    }
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
