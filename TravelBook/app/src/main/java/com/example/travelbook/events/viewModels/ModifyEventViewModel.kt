package com.example.travelbook.events.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.events.models.EventRepository
import kotlinx.coroutines.launch

class ModifyEventViewModel(
    private val repository: EventRepository
) : ViewModel() {
    fun modifyEventItem(eventItem: EventItem) = viewModelScope.launch {
        repository.modifyEvent(eventItem)
    }
}

class ModifyEventViewModelFactory(
    private val repository: EventRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ModifyEventViewModel::class.java)) {
            return ModifyEventViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
