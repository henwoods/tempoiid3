package com.oiid.feature.events.data

import com.oiid.core.model.Event
import com.oiid.core.model.EventDetail
import com.oiid.core.model.api.Resource
import kotlinx.coroutines.flow.Flow

interface EventsService {
    /**
     * Gets the events for a specific artist as a Flow
     */
    fun getEvents(artistId: String): Flow<Resource<List<Event>>>
    
    /**
     * Gets detailed information for a specific event
     */
    suspend fun getEventDetail(artistId: String, eventId: String): Resource<EventDetail>
    
    /**
     * Loads/refreshes events data
     */
    suspend fun loadEvents(artistId: String): Resource<List<Event>>
}