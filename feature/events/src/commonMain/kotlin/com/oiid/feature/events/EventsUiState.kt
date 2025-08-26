package com.oiid.feature.events

import com.oiid.core.model.Event
import com.oiid.core.model.formattedEventTime
import com.oiid.core.model.isInThePast

data class EventUiState(
    val data: Event,
    val formattedTime: String,
    val isInThePast: Boolean = data.isInThePast(),
) {
    val link: String get() = data.link ?: ""
    val venueAddress: String get() = data.venueAddress ?: ""
    val hasTicketLink: Boolean get() = !data.link.isNullOrBlank()
    val hasLocation: Boolean get() = !data.venueAddress.isNullOrBlank()
    val venueName: String get() = data.venueName ?: ""
    val title: String get() = data.title.uppercase()
}

fun Event.toUiState() = EventUiState(
    data = this,
    formattedTime = formattedEventTime().uppercase(),
)

data class EventsUiState(
    val isLoading: Boolean = false,
    val events: List<EventUiState> = emptyList(),
    val error: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EventsUiState) return false
        return isLoading == other.isLoading && events == other.events && error == other.error
    }

    override fun hashCode(): Int {
        return 31 * (31 * isLoading.hashCode() + events.hashCode()) + (error?.hashCode() ?: 0)
    }
}