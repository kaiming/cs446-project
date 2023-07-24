package com.example.travelbook.map.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelbook.UserDataSource
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.events.models.EventRepository
import com.example.travelbook.trips.models.Trip
import com.example.travelbook.trips.models.TripRepository
import kotlinx.coroutines.flow.Flow

class MapViewModel(
    tripRepository: TripRepository,
    private val eventRepository: EventRepository,
    private val userDataSource: UserDataSource
): ViewModel() {
    private val userId = "user3"
    // private val userId = userDataSource.getUserId()

    val tripsFlow: Flow<List<Trip>> = tripRepository.getAllTripsByUserIDFlow(userId)

    fun getEventsFlowByTripId(tripId: String): Flow<List<EventItem>> {
        return eventRepository.getAllEventsByTripIdFlow(tripId)
    }
}

class MapViewModelFactory(
    private val tripRepository: TripRepository,
    private val eventRepository: EventRepository,
    private val userDataSource: UserDataSource
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(tripRepository, eventRepository, userDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}