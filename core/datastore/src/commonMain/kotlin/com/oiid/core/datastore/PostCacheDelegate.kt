package com.oiid.core.datastore

import co.touchlab.kermit.Logger
import com.oiid.core.model.PostComment
import oiid.core.base.datastore.cache.CacheManager
import oiid.core.base.datastore.cache.LruCacheManager

/**
 * Delegate class that handles common caching operations for post comments.
 * This helps reduce code duplication between different service implementations.
 */
class PostCacheDelegate(
    private val postId: String,
    private val cacheKeyPrefix: String,
    private val commentCache: CacheManager<String, List<PostComment>> = LruCacheManager(),
    private val tag: String = "PostCacheDelegate"
) {
    private val cacheKey = "${cacheKeyPrefix}_$postId"

    /**
     * Retrieves comments from cache for the current post.
     * @return Cached comments or null if not found
     */
    fun getCommentsFromCache(): List<PostComment>? {
        return try {
            commentCache.get(cacheKey)
        } catch (e: Exception) {
            Logger.withTag(tag).w(e) { "Failed to get comments from cache for post: $postId" }
            null
        }
    }

    /**
     * Updates the cache with new comments for the current post.
     * @param comments The comments to cache
     */
    fun updateCache(comments: List<PostComment>) {
        try {
            commentCache.put(cacheKey, comments)
            Logger.withTag(tag).d { "Updated cache with ${comments.size} comments for post: $postId" }
        } catch (e: Exception) {
            Logger.withTag(tag).w(e) { "Failed to update cache for post: $postId" }
        }
    }

    /**
     * Adds a new comment to the cache at the beginning of the list.
     * @param newComment The comment to add
     */
    fun addCommentToCache(newComment: PostComment) {
        val cached = getCommentsFromCache()?.toMutableList() ?: mutableListOf()
        cached.add(0, newComment)
        updateCache(cached)
    }

    /**
     * Updates a comment's like status in the cache.
     * @param commentId The ID of the comment to update
     * @param isLiked The new like status
     * @return True if the update was successful
     */
    fun updateCommentLikeInCache(commentId: String, isLiked: Boolean): Boolean {
        val cached = getCommentsFromCache() ?: return false
        val updated = CommentTreeUtils.updateCommentLikeStatus(cached, commentId, isLiked)
        updateCache(updated)
        return true
    }

    /**
     * Adds a reply to a parent comment in the cache.
     * @param parentCommentId The ID of the parent comment
     * @param reply The reply to add
     * @return True if the update was successful
     */
    fun addReplyToCache(parentCommentId: String, reply: PostComment): Boolean {
        val cached = getCommentsFromCache() ?: return false
        val updated = CommentTreeUtils.updateCommentsWithReply(cached, parentCommentId, reply)
        updateCache(updated)
        return true
    }

    /**
     * Updates a comment's flag status in the cache.
     * @param commentId The ID of the comment to flag
     * @param isFlagged The new flag status
     * @return True if the update was successful
     */
    fun updateCommentFlagInCache(commentId: String, isFlagged: Boolean): Boolean {
        val cached = getCommentsFromCache() ?: return false
        val updated = CommentTreeUtils.updateCommentFlagStatus(cached, commentId, isFlagged)
        updateCache(updated)
        return true
    }

    /**
     * Finds a specific comment in the cached comments tree.
     * @param commentId The ID of the comment to find
     * @return The comment if found, null otherwise
     */
    fun findCommentInCache(commentId: String): PostComment? {
        val cached = getCommentsFromCache() ?: return null
        return CommentTreeUtils.findCommentById(cached, commentId)
    }

    /**
     * Clears the cache for the current post.
     */
    fun clearCache() {
        try {
            commentCache.remove(cacheKey)
            Logger.withTag(tag).d { "Cleared cache for post: $postId" }
        } catch (e: Exception) {
            Logger.withTag(tag).w(e) { "Failed to clear cache for post: $postId" }
        }
    }
}