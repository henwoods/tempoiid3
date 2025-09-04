package com.oiid.core.designsystem.composable

import androidx.lifecycle.ViewModel

/**
 * Base ViewModel for preserving scroll state across navigation
 * and process death. Can be extended by specific list ViewModels.
 */
open class ScrollStateViewModel : ViewModel() {
    var scrollIndex: Int = 0
    var scrollOffset: Int = 0

    /**
     * Updates the scroll position
     */
    fun updateScrollPosition(index: Int, offset: Int) {
        scrollIndex = index
        scrollOffset = offset
    }

    /**
     * Resets scroll position to top
     */
    fun resetScrollPosition() {
        scrollIndex = 0
        scrollOffset = 0
    }
}
