package com.example.travelbook.trips.viewModels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travelbook.UserDataSource
import com.example.travelbook.trips.models.Trip
import com.example.travelbook.trips.models.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArchivedTripViewModel(
    private val repository: TripRepository,
    private val userDataSource: UserDataSource
): ViewModel() {
    // private val userId = "user1"
    private val userId = userDataSource.getUserId()

    private val _archivedTripsFlow: MutableStateFlow<List<Trip>> = MutableStateFlow(emptyList())
    val archivedTripsFlow: StateFlow<List<Trip>> get() = _archivedTripsFlow

    fun loadTrips() {
        val userId = userDataSource.getUserId()
        if (userId != null) {
            viewModelScope.launch {
                repository.getAllTripsByUserIdAndFilterForArchivedFlow(userId).collect { trips ->
                    _archivedTripsFlow.value = trips
                }
            }
        }
    }
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