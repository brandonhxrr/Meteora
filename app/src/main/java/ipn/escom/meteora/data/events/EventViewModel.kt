package ipn.escom.meteora.data.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EventViewModel : ViewModel() {

    private val _eventName = MutableLiveData<String>()
    val eventName: LiveData<String> = _eventName

    private val _eventDescription = MutableLiveData<String>()
    val eventDescription: LiveData<String> = _eventDescription

    private val _eventDate = MutableLiveData<String>()
    val eventDate: LiveData<String> = _eventDate

    private val _eventTime = MutableLiveData<String>()
    val eventTime: LiveData<String> = _eventTime

    private val _eventLocation = MutableLiveData<String>()
    val eventLocation: LiveData<String> = _eventLocation

    private val _eventImageUrl = MutableLiveData<String>()
    val eventImageUrl: LiveData<String> = _eventImageUrl

    fun onEventNameChanged(eventName: String) {
        _eventName.value = eventName
    }

    fun onEventDescriptionChanged(eventDescription: String) {
        _eventDescription.value = eventDescription
    }

    fun onEventDateChanged(eventDate: String) {
        _eventDate.value = eventDate
    }

    fun onEventTimeChanged(eventTime: String) {
        _eventTime.value = eventTime
    }

    fun onEventLocationChanged(eventLocation: String) {
        _eventLocation.value = eventLocation
    }



}
