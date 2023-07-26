package com.example.travelbook.budgeting.viewModels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import com.example.travelbook.events.models.EventRepository
import kotlinx.coroutines.launch
import com.example.travelbook.trips.models.TripRepository

class BudgetingViewModel(
    private val tripRepository: TripRepository,
    private val eventRepository: EventRepository
): ViewModel() {

    // To keep track of the total budget of the trip
    var totalTripBudget: String = ""

    // To keep track of the total budget used in the events
    var totalEventBudgets: String = ""

    fun loadBudgets(tripId: String) = viewModelScope.launch {
        tripRepository.getTripByTripIDFlow(tripId).collect { trip ->
            totalTripBudget = trip?.budget?.toFloatOrNull()?.toString() ?: "0"
        }

        eventRepository.getAllEventsByTripIdFlow(tripId).collect { eventList ->
            if(eventList != null) {
                totalEventBudgets = eventList.map { it.cost.toFloatOrNull() ?: 0f }.sum().toString()
            } else {
                totalEventBudgets = "0"
            }
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