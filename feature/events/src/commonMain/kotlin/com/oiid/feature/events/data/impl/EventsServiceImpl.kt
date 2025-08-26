package com.oiid.feature.events.data.impl

import co.touchlab.kermit.Logger
import com.oiid.core.model.Event
import com.oiid.core.model.EventDetail
import com.oiid.core.model.api.Resource
import com.oiid.feature.events.data.EventsService
import com.oiid.network.api.EventsApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class EventsServiceImpl(
    private val eventsApiService: EventsApiService,
) : EventsService {

    private val _eventsCache = MutableStateFlow<Map<String, Resource<List<Event>>>>(emptyMap())

    override fun getEvents(artistId: String): Flow<Resource<List<Event>>> {
        return _eventsCache.asStateFlow().map { cache ->
            cache[artistId] ?: Resource.Loading
        }
    }

    override suspend fun getEventDetail(artistId: String, eventId: String): Resource<EventDetail> {
        return try {
            Logger.d("EventsServiceImpl", Throwable("Fetching event detail for artistId: $artistId, eventId: $eventId"))
            val eventDetail = eventsApiService.getEventDetail(artistId, eventId)
            Resource.Success(eventDetail)
        } catch (e: Exception) {
            Logger.e("EventsServiceImpl: Failed to fetch event detail", e)
            Resource.Error(e)
        }
    }

    override suspend fun loadEvents(artistId: String): Resource<List<Event>> {
        return try {
            Logger.d("EventsServiceImpl", Throwable("Loading events for artistId: $artistId"))

            updateCache(artistId, Resource.Loading)

            val events = eventsApiService.getArtistEvents(artistId)
            val resource = Resource.Success(events)

            updateCache(artistId, resource)

            resource
        } catch (e: Exception) {
            Logger.e("EventsServiceImpl: Failed to load events", e)
            val resource = Resource.Error(e)

            updateCache(artistId, resource)

            resource
        }
    }

    private fun updateCache(artistId: String, resource: Resource<List<Event>>) {
        val currentCache = _eventsCache.value.toMutableMap()
        currentCache[artistId] = resource
        _eventsCache.value = currentCache
    }
}