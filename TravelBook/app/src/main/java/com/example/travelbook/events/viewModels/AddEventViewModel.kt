package com.example.travelbook.events.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.travelbook.UserDataSource
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.events.models.EventRepository
import com.example.travelbook.googlePrediction.models.GooglePredictionRepository
import com.example.travelbook.googlePrediction.models.GooglePredictionResponse
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.trips.models.Trip
import com.example.travelbook.trips.models.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AddEventViewModel(
    private val repository: EventRepository,
    private val tripRepository: TripRepository,
    private val googlePredictionRepository: GooglePredictionRepository,
    private val userDataSource: UserDataSource
): ViewModel() {
    fun addEventItem(newEventItem: EventItem, tripId: String) = viewModelScope.launch {
        repository.addEvent(tripId, newEventItem)
    }

    fun getPredictions(
        input: String
    ): Flow<GooglePredictionResponse> {
        return googlePredictionRepository.getPredictions(input)
    }

    fun getTripByTripId(tripId: String): Flow<Trip?> {
        return tripRepository.getTripByIdFlow(tripId)
    }
}

class AddEventViewModelFactory(
    private val repository: EventRepository,
    private val tripRepository: TripRepository,
    private val googlePredictionRepository: GooglePredictionRepository,
    private val userDataSource: UserDataSource
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEventViewModel::class.java)) {
            return AddEventViewModel(repository, tripRepository, googlePredictionRepository, userDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}