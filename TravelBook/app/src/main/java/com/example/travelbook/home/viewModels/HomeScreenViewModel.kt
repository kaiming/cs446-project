package com.example.travelbook.home.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travelbook.signIn.model.SignInState
import com.example.travelbook.signIn.model.SignInStateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    signInStateRepository: SignInStateRepository
): ViewModel() {
    val isLoggedIn: Boolean = false

//    val viewState = isLoggedIn.map { isLoggedIn ->
//        if(isLoggedIn == null) {
//            ViewState.NotLoggedIn
//        }
//        if(isLoggedIn.state == true) {
//            ViewState.LoggedIn
//        } else if(isLoggedIn.state == false) {
//            ViewState.NotLoggedIn
//        } else {
//            ViewState.Loading
//        }
//    }
}

class HomeScreenViewModelFactory(
    private val repository: SignInStateRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java))
            return HomeScreenViewModel(repository) as T
        throw IllegalArgumentException("Error")
    }
}

sealed class ViewState {
    object Loading: ViewState()
    object LoggedIn: ViewState()
    object NotLoggedIn: ViewState()
}