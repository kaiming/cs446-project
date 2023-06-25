package com.example.travelbook.signIn.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travelbook.signIn.model.SignInState
import com.example.travelbook.signIn.model.SignInStateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SignInViewModel(
    private val repository: SignInStateRepository,
): ViewModel() {
    var currentSignInState: Flow<SignInState> = repository.currentSignInState

    fun addSignInState(newSignInState: SignInState) = viewModelScope.launch {
        repository.insertSignInState(newSignInState)
    }

    fun updateSignInState(signInState: SignInState) = viewModelScope.launch {
        repository.updateSignInState(signInState)
    }
}

class SignInStateModelFactory(
    private val repository: SignInStateRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java))
            return SignInViewModel(repository) as T
        throw IllegalArgumentException("Error")
    }
}