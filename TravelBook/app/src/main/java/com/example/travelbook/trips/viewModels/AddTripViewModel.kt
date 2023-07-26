package com.example.travelbook.trips.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.travelbook.UserDataSource
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.trips.models.Trip
import com.example.travelbook.trips.models.TripRepository
import kotlinx.coroutines.launch

class AddTripViewModel(
    private val repository: TripRepository,
    private val userDataSource: UserDataSource
): ViewModel() {
    private val userId = userDataSource.getUserId()

    fun addTripItem(newTripItem: Trip) = viewModelScope.launch {
        // Add userID to trip item
        val updatedTripItem = newTripItem.copy(participants = listOf(userDataSource.getUserId()!!))
        repository.addTrip(updatedTripItem)
    }
}

class AddTripViewModelFactory(
    private val repository: TripRepository,
    private val userDataSource: UserDataSource
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddTripViewModel::class.java)) {
            return AddTripViewModel(repository, userDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}