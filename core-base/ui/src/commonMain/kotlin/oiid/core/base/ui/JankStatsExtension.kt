package oiid.core.base.ui

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.runtime.Composable

@Composable
expect fun TrackScrollJank(scrollableState: ScrollableState, stateName: String)
