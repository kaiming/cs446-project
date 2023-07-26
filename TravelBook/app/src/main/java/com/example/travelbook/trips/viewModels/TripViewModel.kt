package com.example.travelbook.trips.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.travelbook.UserDataSource
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.trips.models.Trip
import com.example.travelbook.trips.models.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TripViewModel(
    private val repository: TripRepository,
    private val userDataSource: UserDataSource
): ViewModel() {
    // private val userId = "user1"
    // private val userId = userDataSource.getUserId()!!

    private val _tripsFlow: MutableStateFlow<List<Trip>> = MutableStateFlow(emptyList())
    val tripsFlow: StateFlow<List<Trip>> get() = _tripsFlow

    fun loadTrips() {
        val userId = userDataSource.getUserId()
        if (userId != null) {
            viewModelScope.launch {
                repository.getAllTripsByUserIDFlow(userId).collect { trips ->
                    _tripsFlow.value = trips
                }
            }
        }
    }

    fun archiveTrip(tripId: String) {
        repository.archiveTrip(tripId)
        loadTrips()
    }

    fun deleteTrip(tripId: String) {
        repository.deleteTrip(tripId)
        loadTrips()
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