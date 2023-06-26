package com.example.travelbook.events.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.events.models.EventRepository
import com.example.travelbook.events.models.EventResponse
import kotlinx.coroutines.flow.Flow

class EventViewModel(
        private val repository: EventRepository,
        private val navigationController: NavHostController
): ViewModel() {
    private val uripId = "user1" // don't think we need userId
    private val tripId = "trip1"

    val eventsFlow = repository.getAllEventsByTripIdFlow(tripId)

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