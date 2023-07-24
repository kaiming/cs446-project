package com.example.travelbook.trips.viewModels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelbook.UserDataSource
import com.example.travelbook.trips.models.Trip
import com.example.travelbook.trips.models.TripRepository
import kotlinx.coroutines.flow.Flow

class ArchivedTripViewModel(
    private val repository: TripRepository,
    private val userDataSource: UserDataSource
): ViewModel() {
    // private val userId = "user1"
    private val userId = userDataSource.getUserId()

   val archivedTripsFlow: Flow<List<Trip>> = repository.getAllTripsByUserIdAndFilterForArchivedFlow(userDataSource.getUserId()!!)
}

class ArchivedTripViewModelFactory(
    private val repository: TripRepository,
    private val userDataSource: UserDataSource
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArchivedTripViewModel::class.java)) {
            return ArchivedTripViewModel(repository, userDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}