package com.example.travelbook.trips.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.trips.models.Trip
import com.example.travelbook.trips.models.TripRepository
import kotlinx.coroutines.flow.Flow

class TripViewModel(
    repository: TripRepository,
): ViewModel() {
    private val userId = "user1"

    val trips: List<Trip> = repository.getAllTripsByUserID(userId)
    val tripsFlow: Flow<List<Trip>> = repository.getAllTripsByUserIDFlow(userId)
}

class TripViewModelFactory(
    private val repository: TripRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripViewModel::class.java)) {
            return TripViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}