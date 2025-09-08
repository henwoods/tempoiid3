package com.oiid.feature.feed.list

import com.oiid.core.model.PostItem

data class FeedUiState(
    val isLoading: Boolean = false,
    val posts: List<PostItem> = emptyList(),
    val error: String? = null,
    val showCreatePostDialog: Boolean = false,
    val isForum: Boolean,
) {
    val isInitialLoading: Boolean = isLoading && posts.isEmpty() && error == null
    val isRefreshing: Boolean = isLoading && posts.isNotEmpty()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FeedUiState) return false
        return isLoading == other.isLoading &&
                posts == other.posts &&
                error == other.error &&
                showCreatePostDialog == other.showCreatePostDialog
    }

    override fun hashCode(): Int {
        return 31 * (31 * (31 * isLoading.hashCode() + posts.hashCode()) +
                (error?.hashCode() ?: 0)) + showCreatePostDialog.hashCode()
    }
}
