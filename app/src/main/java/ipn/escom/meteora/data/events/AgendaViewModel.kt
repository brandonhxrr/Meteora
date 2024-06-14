package ipn.escom.meteora.data.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import ipn.escom.meteora.data.events.data.network.response.EventResponse
import ipn.escom.meteora.data.events.domain.EventsUseCase
import ipn.escom.meteora.utils.combineDateAndTime
import kotlinx.coroutines.launch
import java.time.Instant

class AgendaViewModel() : ViewModel() {

    private val eventsUseCase = EventsUseCase()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _userId = MutableLiveData<String>()

    private val _userEvents = MutableLiveData<List<EventResponse>>()
    val userEvents: LiveData<List<EventResponse>> = _userEvents

    private val _upcomingEvents = MutableLiveData<List<EventResponse>>()
    val upcomingEvents: LiveData<List<EventResponse>> = _upcomingEvents

    private val _pastEvents = MutableLiveData<List<EventResponse>>()
    val pastEvents: LiveData<List<EventResponse>> = _pastEvents

    private var isAscending = true

    init {
        _userId.value = auth.currentUser?.uid
        _userId.value?.let { getEvents(it) }
    }

    private fun getEvents(userId: String) {
        viewModelScope.launch {
            val events = eventsUseCase(userId)
            _userEvents.postValue(events)
            updateEventLists(events, isAscending)
        }
    }

    fun addEvent(userId: String, event: EventResponse) {
        viewModelScope.launch {
            eventsUseCase(userId, event)
            getEvents(userId)
        }
    }

    fun updateEvent(userId: String, event: EventResponse) {
        viewModelScope.launch {
            eventsUseCase.updateEvent(userId, event)
            getEvents(userId)
        }
    }

    fun deleteEvent(userId: String, eventId: String) {
        eventsUseCase.deleteEvent(userId, eventId)
        val updatedEvents = _userEvents.value?.filter { it.id != eventId }
        _userEvents.postValue(updatedEvents ?: emptyList())
        updateEventLists(updatedEvents ?: emptyList(), isAscending)
    }

    fun onEventOrderChanged(ascending: Boolean) {
        isAscending = ascending
        _userEvents.value?.let { events ->
            updateEventLists(events, ascending)
        }
    }

    private fun updateEventLists(events: List<EventResponse>, ascending: Boolean) {
        val currentUtcDateTime = Instant.now().toEpochMilli()

        val sortedEvents = if (ascending) {
            events.sortedBy { it.date }
        } else {
            events.sortedByDescending { it.date }
        }

        val upcomingEvents = sortedEvents.filter { event ->
            combineDateAndTime(event.date, event.time) >= currentUtcDateTime
        }

        val pastEvents = sortedEvents.filter { event ->
            combineDateAndTime(event.date, event.time) < currentUtcDateTime
        }

        _upcomingEvents.postValue(upcomingEvents)
        _pastEvents.postValue(pastEvents)
    }

}
