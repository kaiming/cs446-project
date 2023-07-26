package com.example.travelbook.trips.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travelbook.UserDataSource
import com.example.travelbook.trips.models.Trip
import com.example.travelbook.trips.models.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ModifyTripViewModel(
    private val repository: TripRepository,
    private val userDataSource: UserDataSource
) : ViewModel() {
    fun getTripById(tripId: String): Flow<Trip?> {
        return repository.getTripByIdFlow(tripId)
    }

    fun modifyTripItem(tripId: String, trip: Trip) = viewModelScope.launch {
        repository.editTrip(tripId, trip)
    }

    fun deleteTripItem(tripId: String) = viewModelScope.launch {
        repository.deleteTrip(tripId)
    }
}

class ModifyTripViewModelFactory(
    private val repository: TripRepository,
    private val userDataSource: UserDataSource
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ModifyTripViewModel::class.java)) {
            return ModifyTripViewModel(repository, userDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
