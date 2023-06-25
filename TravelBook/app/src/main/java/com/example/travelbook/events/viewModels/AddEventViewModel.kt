package com.example.travelbook.events.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.events.models.EventRepository
import kotlinx.coroutines.launch

class AddEventViewModel(
    private val repository: EventRepository
): ViewModel() {

    val tripId: String = "wyj79g8Ye5ILpHVuqr7i"

    fun addEvent(event: EventItem) {
        viewModelScope.launch {
            repository.addEvent(tripId, event)
        }
    }
}

class AddEventViewModelFactory(
    private val repository: EventRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddEventViewModel::class.java))
            return AddEventViewModel(repository) as T
        throw IllegalArgumentException("AddEventViewModel init with incorrect param")
    }
}