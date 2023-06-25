package com.example.travelbook.events.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.events.models.EventRepository
import kotlinx.coroutines.launch

class AddEventViewModel(private val repository: EventRepository): ViewModel() {

    private val tripId: String = "wyj79g8Ye5ILpHVuqr7i"

    fun addEventItem(newEventItem: EventItem) = viewModelScope.launch {
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