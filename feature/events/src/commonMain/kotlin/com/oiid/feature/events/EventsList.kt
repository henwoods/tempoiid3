package com.oiid.feature.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.oiid.core.designsystem.composable.ScrollStateViewModel
import com.oiid.core.designsystem.composable.StatefulLazyColumn
import com.oiid.feature.events.components.EventCard
import oiid.core.base.designsystem.theme.OiidTheme
import oiid.core.base.designsystem.theme.OiidTheme.spacing
import org.koin.compose.viewmodel.koinViewModel

class EventsListViewModel : ScrollStateViewModel()

@Composable
fun EventsList(
    modifier: Modifier = Modifier,
    events: List<EventUiState>,
    onAction: (EventsIntent) -> Unit,
    viewModel: EventsListViewModel = koinViewModel(),
) {
    StatefulLazyColumn(
        modifier = modifier.background(OiidTheme.colorScheme.surface),
        contentPadding = PaddingValues(spacing.md),
        scrollStateViewModel = viewModel,
    ) {
        items(
            items = events,
            key = { event -> event.data.eventId },
            contentType = { "event" },
        ) { event ->
            EventCard(
                event = event,
                modifier = Modifier,
                onAction = onAction,
            )
        }
    }
}
