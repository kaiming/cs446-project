package com.example.travelbook.events.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.events.models.EventRepository
import com.example.travelbook.trips.models.Trip
import com.example.travelbook.trips.models.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EventViewModel(
    private val eventRepository: EventRepository,
    private val tripRepository: TripRepository
): ViewModel() {
    fun getEventsFlowByTripId(tripId: String): Flow<List<EventItem>> {
        return eventRepository.getAllEventsByTripIdFlow(tripId)
    }

    fun getTripByTripId(tripId: String): Flow<Trip?> {
        return tripRepository.getTripByIdFlow(tripId)
    }
}

class EventViewModelFactory(
        private val eventRepository: EventRepository,
        private val tripRepository: TripRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            return EventViewModel(eventRepository, tripRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}