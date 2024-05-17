package ipn.escom.meteora.data.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import ipn.escom.meteora.data.events.data.network.response.EventResponse
import ipn.escom.meteora.data.events.domain.EventsUseCase
import kotlinx.coroutines.launch

class EventViewModel : ViewModel() {

    private val eventsUseCase = EventsUseCase()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> = _userId

    private val _userEvents = MutableLiveData<List<EventResponse>>()
    val userEvents: LiveData<List<EventResponse>> = _userEvents

    private val _eventName = MutableLiveData<String>()
    val eventName: LiveData<String> = _eventName

    private val _eventDescription = MutableLiveData<String>()
    val eventDescription: LiveData<String> = _eventDescription

    private val _eventDate = MutableLiveData<Long>()
    val eventDate: LiveData<Long> = _eventDate

    private val _eventTime = MutableLiveData<Long>()
    val eventTime: LiveData<Long> = _eventTime

    private val _eventLocation = MutableLiveData<String>()
    val eventLocation: LiveData<String> = _eventLocation

    private val _eventImageUrl = MutableLiveData<String>()
    val eventImageUrl: LiveData<String> = _eventImageUrl

    init {
        _userId.value = auth.currentUser?.uid
    }

    fun onEventNameChanged(eventName: String) {
        _eventName.value = eventName
    }

    fun onEventDescriptionChanged(eventDescription: String) {
        _eventDescription.value = eventDescription
    }

    fun onEventDateChanged(eventDate: Long) {
        _eventDate.value = eventDate
    }

    fun onEventTimeChanged(eventTime: Long) {
        _eventTime.value = eventTime
    }

    fun onEventLocationChanged(eventLocation: String) {
        _eventLocation.value = eventLocation
    }

    fun getEvents(userId: String) {
        viewModelScope.launch {
            _userEvents.value = eventsUseCase(userId)
        }
    }

    fun addEvent(userId: String, event: EventResponse) {
        viewModelScope.launch {
            eventsUseCase(userId, event)
        }
    }

}
