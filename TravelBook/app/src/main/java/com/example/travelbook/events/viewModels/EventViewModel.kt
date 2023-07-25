package com.example.travelbook.events.viewModels

import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.events.models.EventRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

class EventViewModel(
    private val repository: EventRepository
): ViewModel() {
    fun getEventsFlowByTripId(tripId: String): Flow<List<EventItem>> {
        return repository.getAllEventsByTripIdFlow(tripId)
    }

    fun handleImageUpload(uri: Uri, tripId: String) {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = formatter.format(Date())
        repository.uploadImageToFirebase(uri, date, tripId)
    }
}

class EventViewModelFactory(
        private val repository: EventRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            return EventViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}