package ipn.escom.meteora.data.events.data.network

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import ipn.escom.meteora.data.events.data.network.response.EventResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class EventsService {

    private val database = Firebase.database.reference

    suspend fun getEvents(userId: String): List<EventResponse> {
        val query = database.child("events").child(userId)
        var listEventResponse: List<EventResponse>? = null

        return withContext(Dispatchers.IO) {
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val events = mutableListOf<EventResponse>()
                    snapshot.children.forEach { event ->
                        val id = event.key
                        val title = event.child("title").value as String
                        val description = event.child("description").value as String
                        val date = event.child("date").value as Long
                        val time = event.child("time").value as Long
                        val location = event.child("location").value as String

                        events.add(EventResponse(id, title, description, date, time, location))
                        listEventResponse = events
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("PredictionsRepository", "Error getting predictions", error.toException())
                }
            })

            while (listEventResponse == null) {
                delay(100)
            }

            listEventResponse ?: emptyList()
        }
    }

    suspend fun addEvent(userId: String, event: EventResponse) {
        val query = database.child("events").child(userId).push()
        query.child("title").setValue(event.title)
        query.child("description").setValue(event.description)
        query.child("date").setValue(event.date)
        query.child("time").setValue(event.time)
        query.child("location").setValue(event.location)
    }

    suspend fun updateEvent(userId: String, event: EventResponse) {
        val query = database.child("events").child(userId).child(event.id!!)
        query.child("title").setValue(event.title)
        query.child("description").setValue(event.description)
        query.child("date").setValue(event.date)
        query.child("time").setValue(event.time)
        query.child("location").setValue(event.location)
    }
}
