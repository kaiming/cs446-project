package com.example.travelbook.budgeting.views


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.launchIn
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.events.models.EventRepository
import com.example.travelbook.googlePrediction.models.GooglePredictionRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.combine
import com.example.travelbook.trips.models.Trip
import com.example.travelbook.trips.models.TripRepository

class BudgetingViewModel(
    private val tripRepository: TripRepository,
    private val eventRepository: EventRepository
): ViewModel() {

    // To keep track of the total budget of the trip
    private var totalTripBudget: String = ""

    // To keep track of the total budget used in the events
    private var totalEventBudgets: String = ""

    fun loadBudgets(tripId: String) = viewModelScope.launch {
        tripRepository.getTripByTripIDFlow(tripId).collect { trip ->
            totalTripBudget = trip?.budget?.toFloatOrNull()?.toString() ?: "0"
        }

        eventRepository.getAllEventsByTripIdFlow(tripId).collect { eventList ->
            totalEventBudgets = eventList.map { it.cost.toFloatOrNull() ?: 0f }.sum().toString()
        }
    }

}

class BudgetingViewModelFactory(
    private val tripRepository: TripRepository,
    private val eventRepository: EventRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetingViewModel::class.java)) {
            return BudgetingViewModel(tripRepository, eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}