package com.example.travelbook.trips.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.travelbook.UserDataSource
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.trips.models.Trip
import com.example.travelbook.trips.models.TripRepository
import kotlinx.coroutines.flow.Flow

class TripViewModel(
    private val repository: TripRepository,
    private val userDataSource: UserDataSource
): ViewModel() {
    // private val userId = "user1"
    // private val userId = userDataSource.getUserId()!!

    val tripsFlow: Flow<List<Trip>> = repository.getAllTripsByUserIDFlow(userDataSource.getUserId()!!)

    fun archiveTrip(tripId: String) {
        repository.archiveTrip(tripId)
    }

    fun deleteTrip(tripId: String) {
        repository.deleteTrip(tripId)
    }
}

class TripViewModelFactory(
    private val repository: TripRepository,
    private val userDataSource: UserDataSource
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripViewModel::class.java)) {
            return TripViewModel(repository, userDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}