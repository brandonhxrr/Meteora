package ipn.escom.meteora.data.events

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import ipn.escom.meteora.data.events.data.network.response.EventResponse
import ipn.escom.meteora.data.events.domain.EventsUseCase
import kotlinx.coroutines.launch

class AgendaViewModel() : ViewModel() {

    private val eventsUseCase = EventsUseCase()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> = _userId

    private val _userEvents = MutableLiveData<List<EventResponse>>()
    val userEvents: LiveData<List<EventResponse>> = _userEvents

    init {
        _userId.value = auth.currentUser?.uid
        _userId.value?.let { getEvents(it) }
    }

    fun getEvents(userId: String) {
        viewModelScope.launch {
            val events = eventsUseCase(userId)
            _userEvents.postValue(events.sortedBy { it.date })
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
        Log.d("EventViewModel", "Updated events: $updatedEvents")
        Log.d("EventViewModel", "User events: ${_userEvents.value}")
        _userEvents.postValue(updatedEvents ?: emptyList())
    }

    fun onEventOrderChanged(ascending: Boolean) {
        viewModelScope.launch {
            val sortedList = if (ascending) {
                _userEvents.value?.sortedBy { it.date }
            } else {
                _userEvents.value?.sortedByDescending { it.date }
            }
            sortedList?.let {
                _userEvents.postValue(it)
            }
        }
    }
}
