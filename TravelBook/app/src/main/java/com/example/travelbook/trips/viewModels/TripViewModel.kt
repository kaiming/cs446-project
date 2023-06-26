package com.example.travelbook.trips.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.trips.models.Trip
import com.example.travelbook.trips.models.TripRepository
import kotlinx.coroutines.flow.Flow

class TripViewModel(
    private val repository: TripRepository,
    private val navigationController: NavHostController
): ViewModel() {
    private val userId = "user1"

    val trips: List<Trip> = repository.getAllTripsByUserID(userId)
    val tripsFlow: Flow<List<Trip>> = repository.getAllTripsByUserIDFlow(userId)

    fun onAddTripClicked() {
        navigationController.navigate(NavigationItem.AddTrip.route)
    }
}

class TripViewModelFactory(
    private val repository: TripRepository,
    private val navigationController: NavHostController
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripViewModel::class.java)) {
            return TripViewModel(repository, navigationController) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}