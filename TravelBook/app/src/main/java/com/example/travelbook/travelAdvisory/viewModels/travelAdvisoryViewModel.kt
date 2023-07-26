package com.example.travelbook.travelAdvisory.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.events.models.EventRepository
import kotlinx.coroutines.flow.Flow
import com.example.travelbook.travelAdvisory.models.Advisory
import com.example.travelbook.travelAdvisory.models.TravelAdvisoryRepository

class TravelAdvisoryViewModel(
    private val eventRepository: EventRepository,
    private val travelAdvisoryRepository: TravelAdvisoryRepository
): ViewModel() {
    fun getEventsFlowByTripId(tripId: String): Flow<List<EventItem>> {
        return eventRepository.getAllEventsByTripIdFlow(tripId)
    }

    fun getTravelAdvisories(countryCodes: List<String>): Flow<Map<String, Advisory>> {
        return travelAdvisoryRepository.getTravelAdvisories(countryCodes)
    }
}

class TravelAdvisoryViewModelFactory(
    private val eventRepository: EventRepository,
    private val travelAdvisoryRepository: TravelAdvisoryRepository,
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TravelAdvisoryViewModel::class.java)) {
            return TravelAdvisoryViewModel(eventRepository, travelAdvisoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}