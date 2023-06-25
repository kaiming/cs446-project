package com.example.travelbook.signIn.model

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class SignInStateRepository(
    private val signInStateDao: SignInStateDao
) {
    val currentSignInState: Flow<SignInState> = signInStateDao.currentSignInState()

    @WorkerThread
    suspend fun insertSignInState(signInState: SignInState) {
        signInStateDao.insertSignInState(signInState)
    }

    @WorkerThread
    suspend fun updateSignInState(signInState: SignInState) {
        signInStateDao.updateSignInState(signInState)
    }
}