package com.example.travelbook.trips.viewModels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelbook.trips.models.Trip
import com.example.travelbook.trips.models.TripRepository
import kotlinx.coroutines.flow.Flow

class ArchivedTripViewModel(
    private val repository: TripRepository
): ViewModel() {
    private val userId = "user1" // TODO: temp

    val archivedTripsFlow: Flow<List<Trip>> = repository.getAllTripsByUserIdAndFilterForArchivedFlow(userId)
}

class ArchivedTripViewModelFactory(
    private val repository: TripRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArchivedTripViewModel::class.java)) {
            return ArchivedTripViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}