package com.example.travelbook.trips.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.events.models.EventRepository
import kotlinx.coroutines.flow.Flow

class EventViewModel(
        private val repository: EventRepository,
        private val navigationController: NavHostController
): ViewModel() {
    private val uripId = "user1"
    private val tripId = "trip1"

    val events: List<Event> = repository.getAllEventsByTripId(userId, tripId)
    val eventsFlow: Flow<List<Event>> = repository.getAllEventsByTripIdFlow(userId, tripId)

    fun onAddEventClicked() {
        navigationController.navigate(NavigationItem.AddEvent.route)
    }
}

class EventViewModelFactory(
        private val repository: EventRepository,
        private val navigationController: NavHostController
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            return EventViewModel(repository, navigationController) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}