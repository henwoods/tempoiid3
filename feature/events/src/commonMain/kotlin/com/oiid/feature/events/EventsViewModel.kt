package com.oiid.feature.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oiid.core.config.artistId
import com.oiid.core.model.Event
import com.oiid.core.model.api.Resource
import com.oiid.core.model.ui.UiEvent
import com.oiid.feature.events.data.EventsService
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface EventsIntent {
    data object LoadEvents : EventsIntent
    data object RetryLoad : EventsIntent
    data class EventClicked(val event: Event) : EventsIntent
    data class ErrorOccurred(val message: String) : EventsIntent
    data class TicketClicked(val url: String?) : EventsIntent
    data class MapClicked(val event: Event) : EventsIntent
}

class EventsViewModel(
    private val eventsService: EventsService,
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent

    @OptIn(FlowPreview::class)
    val uiState: StateFlow<EventsUiState> = eventsService.getEvents(artistId()).map { resource ->
        when (resource) {
            is Resource.Loading -> EventsUiState(isLoading = true)
            is Resource.Success -> EventsUiState(
                events = resource.data.sortedBy { it.eventTime }
                    .map { it.toUiState() }.filter { !it.isInThePast },
            )

            is Resource.Error -> EventsUiState(
                error = resource.exception.message ?: "Failed to load events.",
            )
        }
    }.distinctUntilChanged().catch { emit(EventsUiState(error = "Failed to load events.")) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EventsUiState(isLoading = true),
    )

    init {
        viewModelScope.launch {
            eventsService.loadEvents(artistId())
        }
    }

    fun handleIntent(intent: EventsIntent) {
        when (intent) {
            is EventsIntent.LoadEvents -> loadEvents()
            is EventsIntent.RetryLoad -> retryLoad()
            is EventsIntent.EventClicked -> {}
            is EventsIntent.ErrorOccurred -> showSnackbar(message = intent.message)
            is EventsIntent.TicketClicked -> handleTicketClick(url = intent.url)
            is EventsIntent.MapClicked -> handleMapClick(event = intent.event)
        }
    }

    private fun handleTicketClick(url: String?) = viewModelScope.launch {
        if (!url.isNullOrBlank()) {
            _uiEvent.emit(UiEvent.LaunchUri(url))
        } else {
            _uiEvent.emit(UiEvent.ShowSnackbar("No ticket link available"))
        }
    }

    private fun handleMapClick(event: Event) = viewModelScope.launch {
        val mapUrl = event.buildMapUrl()
        if (mapUrl.isNotBlank()) {
            try {
                _uiEvent.emit(UiEvent.LaunchUri(mapUrl))
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowSnackbar("Could not open maps, $e"))
            }
        } else {
            _uiEvent.emit(UiEvent.ShowSnackbar("No venue address available"))
        }
    }

    private fun showSnackbar(message: String) = viewModelScope.launch {
        _uiEvent.emit(UiEvent.ShowSnackbar(message))
    }

    private fun loadEvents() = viewModelScope.launch {
        eventsService.loadEvents(artistId())
    }

    fun retryLoad() = viewModelScope.launch {
        eventsService.loadEvents(artistId())
    }
}