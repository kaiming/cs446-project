package com.example.travelbook.map.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travelbook.UserDataSource
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.events.models.EventRepository
import com.example.travelbook.trips.models.Trip
import com.example.travelbook.trips.models.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapViewModel(
    private val tripRepository: TripRepository,
    private val eventRepository: EventRepository,
    private val userDataSource: UserDataSource
): ViewModel() {

    private val _tripsFlow: MutableStateFlow<List<Trip>> = MutableStateFlow(emptyList())
    val tripsFlow: StateFlow<List<Trip>> get() = _tripsFlow

    fun loadTrips() {
        val userId = userDataSource.getUserId()
        if (userId != null) {
            viewModelScope.launch {
                tripRepository.getAllTripsByUserIDFlow(userId).collect { trips ->
                    _tripsFlow.value = trips
                }
            }
        }
    }

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