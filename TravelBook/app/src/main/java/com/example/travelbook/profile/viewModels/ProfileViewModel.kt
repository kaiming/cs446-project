package com.example.travelbook.profile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.travelbook.User
import com.example.travelbook.UserDataSource
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.signIn.AuthRepo
import com.example.travelbook.signIn.viewModels.NewSignInViewModel

class ProfileViewModel (
    private val navigationController: NavHostController,
    private val userDataSource: UserDataSource,
    private val repository: AuthRepo
) : ViewModel() {
    fun onSignOutClicked() {
        repository.signOut()
        userDataSource.deleteUser()
        navigationController.navigate(NavigationItem.SignIn.route)
    }

    fun getUser(): User? {
        return userDataSource.getUser()
    }
}

class ProfileViewModelFactory(
    private val navigationController: NavHostController, private val signInRepository: AuthRepo, private val userDataSource: UserDataSource
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(navigationController, userDataSource, signInRepository) as T
        }
        throw IllegalArgumentException("ProfileViewModel init will incorrect param")
    }
}