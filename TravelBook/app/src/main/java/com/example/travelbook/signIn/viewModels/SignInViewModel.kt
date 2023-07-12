package com.example.travelbook.signIn.viewModels

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.signIn.SignInRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInViewModel(
    private val navigationController: NavHostController, private val repository: SignInRepo
): ViewModel() {
    fun onSignInClicked() {
        navigationController.navigate(NavigationItem.Map.route)
    }

    fun firebaseAuthWithEmailAndPassword(email: String, password: String, context: Context) {
        viewModelScope.launch {
            val authResult = repository.signInWithEmailAndPassword(email, password)
            if (authResult.isSuccessful) {

                val user = authResult.result.user

                // should never hit
                if (user == null) {
                    // error state
                }

                val sharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
                withContext(Dispatchers.IO) {
                    user?.let {
                        sharedPreferences.edit {
                            putString("USER_ID", it.uid)
                            putString("USER_NAME", it.displayName)
                            putString("USER_EMAIL", it.email)
                        }
                    }
                }

                navigationController.navigate(NavigationItem.Map.route)
            }
        }
    }

    // incomplete
    fun firebaseAuthWithGoogle(idToken: String?) {
        repository.firebaseAuthWithGoogle(idToken)
    }
}

class SignInViewModelFactory(
    private val navigationController: NavHostController, private val signInRepository: SignInRepo
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SignInViewModel::class.java))
            return SignInViewModel(navigationController, signInRepository) as T
        throw IllegalArgumentException("SignInViewModel init will incorrect param")
    }
}