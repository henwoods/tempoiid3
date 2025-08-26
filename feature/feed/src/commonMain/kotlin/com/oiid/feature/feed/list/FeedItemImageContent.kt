package com.oiid.feature.feed.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import com.oiid.core.designsystem.Dimens.Companion.POST_MEDIA_HEIGHT
import com.oiid.core.designsystem.composable.FiveLinesProgress
import oiid.core.base.ui.rememberImageLoader

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageContent(modifier: Modifier = Modifier, imageUrls: List<String>) {
    val imageLoader = rememberImageLoader(LocalPlatformContext.current)
    val pagerState = rememberPagerState { imageUrls.size }
    HorizontalPager(state = pagerState, modifier = modifier.fillMaxWidth().height(POST_MEDIA_HEIGHT)) { page ->
        SubcomposeAsyncImage(
            model = imageUrls[page],
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = modifier.fillMaxSize(),
            imageLoader = imageLoader,
            loading = { FiveLinesProgress() },
        )
    }
}
