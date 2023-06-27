package com.example.travelbook.events.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.events.models.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EventViewModel(
    private val repository: EventRepository
): ViewModel() {
    fun getEventsFlowByTripId(tripId: String): Flow<List<EventItem>> {
        return repository.getAllEventsByTripIdFlow(tripId)
    }
}

class EventViewModelFactory(
        private val repository: EventRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            return EventViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}