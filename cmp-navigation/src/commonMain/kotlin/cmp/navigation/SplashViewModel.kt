package cmp.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oiid.feature.feed.data.impl.FeedServiceImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Very simple class to help determine if the loading foreground blur should displays. State will be updated to false
 * if there is cached data.
 */
class SplashViewModel(
    private val feedService: FeedServiceImpl,
) : ViewModel() {
    private val _cacheIsEmpty = MutableStateFlow(true)
    val cacheIsEmpty: StateFlow<Boolean> = _cacheIsEmpty.asStateFlow()

    init {
        viewModelScope.launch {
            _cacheIsEmpty.value = feedService.getCachedPostsSnapshot().isNullOrEmpty()
        }
    }
}
