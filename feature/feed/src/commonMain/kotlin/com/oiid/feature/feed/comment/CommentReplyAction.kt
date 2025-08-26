package com.oiid.feature.feed.comment

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import com.oiid.core.model.PostComment

@Deprecated("Using global reply for now")
@Composable
fun CommentReplyAction(
    focusRequester: FocusRequester,
    comment: PostComment,
    onReplySubmitted: (String, String) ->
    Unit,
) {
    var isReplying by remember { mutableStateOf(false) }
    var replyText by remember { mutableStateOf("") }
    val userCanReplyToComments = false
    if (userCanReplyToComments) {
        Text(
            text = "Reply",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.clickable { isReplying = !isReplying },
        )

        if (isReplying) {
            ReplyInput(
                value = replyText,
                onValueChange = { replyText = it },
                label = "Your reply...",
                focusRequester = focusRequester,
                onSendClick = { onReplySubmitted(comment.commentId, replyText) },
                onCancel = {
                    isReplying = false
                    replyText = ""
                },
            )
        }
    }
}
