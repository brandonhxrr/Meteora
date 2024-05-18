package ipn.escom.meteora.data.events.data

import ipn.escom.meteora.data.events.data.network.EventsService
import ipn.escom.meteora.data.events.data.network.response.EventResponse

class EventsRepository {

    private val service = EventsService()

    suspend fun getEvents(userId: String) = service.getEvents(userId)

    suspend fun addEvent(userId: String, event: EventResponse) = service.addEvent(userId, event)

    suspend fun updateEvent(userId: String, event: EventResponse) = service.updateEvent(userId, event)
}
