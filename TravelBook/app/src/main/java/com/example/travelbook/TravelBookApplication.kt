package com.example.travelbook

import android.app.Application
import com.example.travelbook.signIn.model.SignInStateDatabase
import com.example.travelbook.signIn.model.SignInStateRepository


class TravelBookApplication: Application() {
    // TODO: Add in databases and repositories here
    private val signInStateDatabase by lazy { SignInStateDatabase.getDatabase(this) }
    val signInStateRepository by lazy { SignInStateRepository(signInStateDatabase.SignInStateDao())}
}