package com.example.travelbook.events.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.events.models.EventRepository
import com.example.travelbook.navigation.models.NavigationItem
import kotlinx.coroutines.launch

class AddEventViewModel(
    private val repository: EventRepository
): ViewModel() {
    fun addEventItem(newEventItem: EventItem, tripId: String) = viewModelScope.launch {
        repository.addEvent(tripId, newEventItem)
    }
}

class AddEventViewModelFactory(
    private val repository: EventRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEventViewModel::class.java)) {
            return AddEventViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}