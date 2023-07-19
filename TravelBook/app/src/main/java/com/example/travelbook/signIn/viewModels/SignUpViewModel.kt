package com.example.travelbook.signIn.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.signIn.AuthRepo
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpViewModel(
    private val navigationController: NavHostController, private val repository: AuthRepo
): ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> = _errorMessage

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun signUp() {
        val email = _email.value.trim()
        val password = _password.value

        if (email.isEmpty() || password.isEmpty()) {
            _errorMessage.value = "Please enter email and password"
            return
        }

        viewModelScope.launch {
            try {
                val result = repository.signUpWithEmailAndPassword(email, password)
                handleSignUpResult(result)
            } catch (e: Exception) {
                _errorMessage.value = "Sign-up failed: ${e.message}"
            }
        }
    }

    private fun handleSignUpResult(result: Task<AuthResult>) {
        // Handle successful sign-up here
        navigationController.navigate(NavigationItem.SignIn.route)
    }
}

class SignUpViewModelFactory(
    private val navigationController: NavHostController, private val authRepo: AuthRepo
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(navigationController, authRepo) as T
        }
        throw IllegalArgumentException("SignUpViewModel init will incorrect param")
    }
}
