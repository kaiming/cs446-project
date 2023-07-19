package com.example.travelbook.events.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.events.models.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ModifyEventViewModel(
    private val repository: EventRepository
) : ViewModel() {
    fun getEventById(tripId: String, eventId: String): Flow<EventItem?> {
        return repository.getEventByIdFlow(tripId, eventId)
    }

    fun modifyEventItem(tripId: String, eventId: String, eventItem: EventItem) = viewModelScope.launch {
        repository.editEvent(tripId, eventId, eventItem)
    }

    fun deleteEventItem(tripId: String, eventId: String) = viewModelScope.launch {
        repository.deleteEvent(tripId, eventId)
    }
}

class ModifyEventViewModelFactory(
    private val repository: EventRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ModifyEventViewModel::class.java)) {
            return ModifyEventViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
