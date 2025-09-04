package com.oiid.feature.events.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.oiid.core.designsystem.composable.OiidBodyText
import com.oiid.core.designsystem.composable.OiidEventButton
import com.oiid.core.designsystem.composable.OiidIconButton
import com.oiid.core.designsystem.composable.OiidTitleText
import com.oiid.core.designsystem.composable.OiidTitleTextItalic
import com.oiid.core.designsystem.diagonalCornerShape
import com.oiid.feature.events.EventUiState
import com.oiid.feature.events.EventsIntent
import oiid.core.base.designsystem.theme.OiidTheme.spacing

@Composable
fun EventCardHeader(
    event: EventUiState,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.Center,

    ) {
        OiidTitleText(text = event.title)
        OiidTitleTextItalic(text = event.formattedTime)
    }
}

@Composable
fun EventCard(
    modifier: Modifier = Modifier,
    event: EventUiState,
    onAction: (EventsIntent) -> Unit,
) {
    Card(
        modifier = modifier,
        shape = diagonalCornerShape(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        onClick = {
            onAction(EventsIntent.EventClicked(event.data))
        },
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(spacing.lg),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                EventCardHeader(event)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                ) {
                    OiidBodyText(
                        text = event.venueName,
                    )

                    if (event.hasLocation) {
                        OiidIconButton(
                            modifier = Modifier.size(40.dp),
                            onClick = {
                                onAction(EventsIntent.MapClicked(event.data))
                            },
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Open in maps",
                        )
                    }
                }

                if (event.hasTicketLink) {
                    OiidEventButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = "BUY TICKET",
                        isEnabled = !event.isInThePast,
                        onClick = {
                            onAction(EventsIntent.TicketClicked(event.link))
                        },
                    )
                }
            }
        }
    }
}
