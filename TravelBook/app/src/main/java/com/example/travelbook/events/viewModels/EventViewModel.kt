package com.example.travelbook.events.viewModels

import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.travelbook.UserDataSource
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.events.models.EventRepository
import com.example.travelbook.trips.models.Trip
import com.example.travelbook.trips.models.TripRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

class EventViewModel(
    private val eventRepository: EventRepository,
    private val tripRepository: TripRepository,
    private val userDataSource: UserDataSource
): ViewModel() {
    fun getEventsFlowByTripId(tripId: String): Flow<List<EventItem>> {
        return eventRepository.getAllEventsByTripIdFlow(tripId)
    }

    fun getTripByTripId(tripId: String): Flow<Trip?> {
        return tripRepository.getTripByIdFlow(tripId)
    }

    fun handleImageUpload(uri: Uri, tripId: String) {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = formatter.format(Date())
        try {
            eventRepository.uploadImageToFirebase(uri, date, tripId)
        } catch (e: Exception) {
            Log.d("UPLOAD_ERROR", "Failed to upload image.")
        }
    }
}

class EventViewModelFactory(
        private val eventRepository: EventRepository,
        private val tripRepository: TripRepository,
        private val userDataSource: UserDataSource
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            return EventViewModel(eventRepository, tripRepository, userDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}