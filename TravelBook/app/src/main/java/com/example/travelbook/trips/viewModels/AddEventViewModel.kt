package com.example.travelbook.trips.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travelbook.trips.models.EventItem
import com.example.travelbook.trips.models.EventRepository
import kotlinx.coroutines.launch

class AddEventViewModel(private val repository: EventRepository): ViewModel() {

    fun addEventItem(tripId: String, newEventItem: EventItem) = viewModelScope.launch {
        repository.addEvent(tripId, newEventItem)
    }
}

class AddEventViewModelFactory(private val repository: EventRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEventViewModel::class.java)) {
            return AddEventViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}