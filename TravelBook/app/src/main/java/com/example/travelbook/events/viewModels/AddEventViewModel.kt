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
    private val repository: EventRepository,
    private val navigationController: NavHostController
): ViewModel() {

    private val tripId: String = "pDzQFXKFajXwy3OIAuD2"

    fun addEventItem(newEventItem: EventItem) = viewModelScope.launch {
        repository.addEvent(tripId, newEventItem)
        navigationController.navigate(NavigationItem.Event.route)
    }
}

class AddEventViewModelFactory(
    private val repository: EventRepository,
    private val navigationController: NavHostController
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEventViewModel::class.java)) {
            return AddEventViewModel(repository, navigationController) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}