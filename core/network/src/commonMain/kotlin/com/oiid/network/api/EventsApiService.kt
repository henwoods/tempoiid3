package com.oiid.network.api

import com.oiid.core.model.Event
import com.oiid.core.model.EventDetail
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path

interface EventsApiService {
    /**
     * Fetches the events for a specific artist.
     * @param artistId The unique identifier for the artist (e.g., "artist_6e525763").
     * @return A list of [Event] objects.
     */
    @GET("app/artists/{artistId}/events")
    suspend fun getArtistEvents(@Path("artistId") artistId: String): List<Event>

    /**
     * Fetches details for a specific event.
     * @param artistId The unique identifier for the artist.
     * @param eventId The unique identifier for the event.
     * @return An [EventDetail] object.
     */
    @GET("app/artists/{artistId}/events/{eventId}")
    suspend fun getEventDetail(
        @Path("artistId") artistId: String,
        @Path("eventId") eventId: String,
    ): EventDetail
}
