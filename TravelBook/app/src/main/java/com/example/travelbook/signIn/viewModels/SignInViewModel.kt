package com.example.travelbook.signIn.viewModels

import android.content.Intent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.signIn.signInRepository
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.coroutines.launch

class SignInViewModel(
    private val navigationController: NavHostController, private val repository: signInRepository
): ViewModel() {
    fun onSignInClicked() {
        navigationController.navigate(NavigationItem.Map.route)
    }

//    fun checkUserSignedIn() {
//        repository.getFirebaseAuth().addAuthStateListener { firebaseAuth ->
//            val user = firebaseAuth.currentUser
//            if (user != null) {
//                // User is signed in
//                prefs.signedInUser = user.uid
//            } else {
//                // User is signed out
//                prefs.signedInUser = null
//            }
//        }
//    }

    fun firebaseAuthWithEmailAndPassword(email: String, password: String) {
        repository.signInWithEmailAndPassword(email, password)
    }

    fun firebaseAuthWithGoogle(idToken: String?) {
        repository.firebaseAuthWithGoogle(idToken)
    }
}

class SignInViewModelFactory(
    private val navigationController: NavHostController, private val signInRepository: signInRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SignInViewModel::class.java))
            return SignInViewModel(navigationController, signInRepository) as T
        throw IllegalArgumentException("SignInViewModel init will incorrect param")
    }
}