package com.oiid.feature.events

import com.oiid.core.model.Event

fun Event.buildMapUrl(): String {
    val query = if (!venueName.isNullOrBlank() && venueName != venueAddress) {
        "$venueName, $venueAddress"
    } else {
        venueAddress
    }

    if (query.isNullOrBlank()) {
        return ""
    }

    val encodedQuery = query.replace(" ", "%20").replace(",", "%2C")

    return "https://www.google.com/maps/search/?api=1&query=$encodedQuery"
}