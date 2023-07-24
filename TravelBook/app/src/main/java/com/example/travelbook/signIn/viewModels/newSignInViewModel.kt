package com.example.travelbook.signIn.viewModels

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.travelbook.User
import com.example.travelbook.UserDataSource
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.signIn.AuthRepo
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NewSignInViewModel(
    private val navigationController: NavHostController,
    private val userDataSource: UserDataSource,
    private val repository: AuthRepo
) : ViewModel() {
    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun handleAuthResult(firebaseUser: FirebaseUser?) {
        if (firebaseUser != null) {
            // Sign-in successful, handle the signed-in user
            val user = User(
                id = firebaseUser?.uid ?: "",
                name = firebaseUser?.displayName,
                email = firebaseUser?.email
            )
            Log.d("NewSignInViewModel", "User: $user")
            userDataSource.saveUser(user)
            repository.saveUserInFirebase(user)
            navigationController.navigate(NavigationItem.Map.route)
        } else {
            // User is null, handle the failure
            _errorMessage.value = "Sign-in failed"
        }
        _loadingState.value = false
    }

    fun onSignUpClicked() {
        navigationController.navigate(NavigationItem.SignUp.route)
    }

    fun handleSignInFailure(errorMessage: String) {
        _errorMessage.value = errorMessage
        _loadingState.value = false
    }

    fun setLoadingState(loading: Boolean) {
        _loadingState.value = loading
    }
}


class NewSignInViewModelFactory(
    private val navigationController: NavHostController, private val signInRepository: AuthRepo, private val userDataSource: UserDataSource
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NewSignInViewModel::class.java)) {
            return NewSignInViewModel(navigationController, userDataSource, signInRepository) as T
        }
        throw IllegalArgumentException("SignInViewModel init will incorrect param")
    }
}
