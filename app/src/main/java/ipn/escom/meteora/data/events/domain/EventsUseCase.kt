package ipn.escom.meteora.data.events.domain

import ipn.escom.meteora.data.events.data.EventsRepository
import ipn.escom.meteora.data.events.data.network.response.EventResponse

class EventsUseCase {
    private val repository = EventsRepository()

    suspend operator fun invoke(userId: String): List<EventResponse> {
        return repository.getEvents(userId)
    }

    suspend operator fun invoke(userId: String, event: EventResponse) {
        repository.addEvent(userId, event)
    }

    suspend fun updateEvent(userId: String, event: EventResponse) {
        repository.updateEvent(userId, event)
    }

    fun deleteEvent(userId: String, eventId: String) {
        repository.deleteEvent(userId, eventId)
    }
}