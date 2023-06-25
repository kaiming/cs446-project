package com.example.travelbook.signIn.viewModels

import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.travelbook.navigation.models.NavigationItem
import kotlinx.coroutines.launch

class SignInViewModel(
    private val navigationController: NavHostController
): ViewModel() {
    fun onSignInClicked() {
        navigationController.navigate(NavigationItem.Map.route)
    }
}

class SignInViewModelFactory(
    private val navigationController: NavHostController
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SignInViewModel::class.java))
            return SignInViewModel(navigationController) as T
        throw IllegalArgumentException("SignInViewModel init will incorrect param")
    }
}