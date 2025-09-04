package com.oiid.core.datastore

import com.oiid.core.model.PostComment

/**
 * Utility object for common operations on comment trees.
 * This helps reduce code duplication between PostService and ForumPostService implementations.
 */
object CommentTreeUtils {

    /**
     * Finds a comment by ID in a hierarchical comment tree.
     * @param comments The list of comments to search in
     * @param commentId The ID of the comment to find
     * @return The found comment or null if not found
     */
    fun findCommentById(comments: List<PostComment>, commentId: String): PostComment? {
        comments.forEach { comment ->
            if (comment.commentId == commentId) return comment
            val found = findCommentById(comment.comments, commentId)
            if (found != null) return found
        }
        return null
    }

    /**
     * Adds a reply to the appropriate parent comment in a comment tree.
     * @param comments The list of comments to update
     * @param parentCommentId The ID of the parent comment
     * @param reply The reply comment to add
     * @return Updated list of comments with the reply added
     */
    fun updateCommentsWithReply(
        comments: List<PostComment>,
        parentCommentId: String,
        reply: PostComment,
    ): List<PostComment> {
        return comments.map { comment ->
            when {
                comment.commentId == parentCommentId -> {
                    comment.copy(comments = comment.comments + reply)
                }
                comment.comments.isNotEmpty() -> {
                    comment.copy(comments = updateCommentsWithReply(comment.comments, parentCommentId, reply))
                }
                else -> comment
            }
        }
    }

    /**
     * Updates the like status of a specific comment in a comment tree.
     * @param comments The list of comments to update
     * @param commentId The ID of the comment to update
     * @param isLiked The new like status
     * @return Updated list of comments with the like status changed
     */
    fun updateCommentLikeStatus(
        comments: List<PostComment>,
        commentId: String,
        isLiked: Boolean,
    ): List<PostComment> {
        return comments.map { comment ->
            when {
                comment.commentId == commentId -> {
                    comment.copy(
                        isLikedByUser = isLiked,
                        numberOfLikes = comment.numberOfLikes + if (isLiked) 1 else -1,
                    )
                }
                comment.comments.isNotEmpty() -> {
                    comment.copy(comments = updateCommentLikeStatus(comment.comments, commentId, isLiked))
                }
                else -> comment
            }
        }
    }

    /**
     * Updates the flag status of a specific comment in a comment tree.
     * @param comments The list of comments to update
     * @param commentId The ID of the comment to flag
     * @param isFlagged The new flag status
     * @return Updated list of comments with the flag status changed
     */
    fun updateCommentFlagStatus(
        comments: List<PostComment>,
        commentId: String,
        isFlagged: Boolean,
    ): List<PostComment> {
        return comments.map { comment ->
            when {
                comment.commentId == commentId -> {
                    comment.copy(flagged = isFlagged)
                }
                comment.comments.isNotEmpty() -> {
                    comment.copy(comments = updateCommentFlagStatus(comment.comments, commentId, isFlagged))
                }
                else -> comment
            }
        }
    }

    /**
     * Counts the total number of comments in a comment tree (including nested replies).
     * @param comments The list of comments to count
     * @return Total number of comments including all nested replies
     */
    fun countTotalComments(comments: List<PostComment>): Int {
        return comments.sumOf { comment ->
            1 + countTotalComments(comment.comments)
        }
    }

    /**
     * Flattens a hierarchical comment tree into a single list while preserving parent-child relationships.
     * @param comments The hierarchical comment list
     * @param level The indentation level (starts at 0 for top-level comments)
     * @return A flattened list of comments with level information
     */
    fun flattenCommentTree(comments: List<PostComment>, level: Int = 0): List<Pair<PostComment, Int>> {
        return comments.flatMap { comment ->
            listOf(comment to level) + flattenCommentTree(comment.comments, level + 1)
        }
    }
}
