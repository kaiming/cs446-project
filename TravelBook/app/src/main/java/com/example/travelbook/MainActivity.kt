package com.example.travelbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.travelbook.navigation.views.NavigationView
import com.example.travelbook.signIn.viewModels.SignInViewModel
import com.example.travelbook.signIn.viewModels.SignInViewModelFactory
import com.example.travelbook.signIn.viewModels.SignUpViewModelFactory
import com.example.travelbook.events.models.EventRepository
import com.example.travelbook.trips.models.TripRepository
import com.example.travelbook.events.viewModels.AddEventViewModel
import com.example.travelbook.events.viewModels.AddEventViewModelFactory
import com.example.travelbook.events.viewModels.ModifyEventViewModel
import com.example.travelbook.events.viewModels.ModifyEventViewModelFactory
import com.example.travelbook.events.viewModels.EventViewModel
import com.example.travelbook.events.viewModels.EventViewModelFactory
import com.example.travelbook.googlePrediction.models.GooglePredictionRepository
import com.example.travelbook.map.viewModels.MapViewModel
import com.example.travelbook.map.viewModels.MapViewModelFactory
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.profile.viewModels.ProfileViewModel
import com.example.travelbook.profile.viewModels.ProfileViewModelFactory
import com.example.travelbook.signIn.AuthRepo
import com.example.travelbook.signIn.viewModels.NewSignInViewModel
import com.example.travelbook.signIn.viewModels.NewSignInViewModelFactory
import com.example.travelbook.signIn.viewModels.SignUpViewModel
import com.example.travelbook.trips.viewModels.AddTripViewModel
import com.example.travelbook.trips.viewModels.AddTripViewModelFactory
import com.example.travelbook.trips.viewModels.ArchivedTripViewModel
import com.example.travelbook.trips.viewModels.ArchivedTripViewModelFactory
import com.example.travelbook.trips.viewModels.ModifyTripViewModel
import com.example.travelbook.trips.viewModels.ModifyTripViewModelFactory
import com.example.travelbook.trips.viewModels.TripViewModel
import com.example.travelbook.trips.viewModels.TripViewModelFactory
import com.example.travelbook.ui.theme.TravelBookTheme
import com.google.android.libraries.places.api.Places
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tripRepo = TripRepository()

        // // Disable caching and offline persistence
        // val firestoreSettings = FirebaseFirestoreSettings.Builder()
        //     .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED) // Disable caching
        //     .setPersistenceEnabled(false) // Disable offline persistence
        //     .build()

        // val firestore = Firebase.firestore
        // firestore.firestoreSettings = firestoreSettings

        setContent {

            Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)

            val navigationController = rememberNavController()

            val signInViewModel: SignInViewModel by viewModels {
                SignInViewModelFactory(navigationController, AuthRepo())
            }

            val profileViewModel: ProfileViewModel by viewModels {
                ProfileViewModelFactory(
                    navigationController,
                    AuthRepo(),
                    SharedPreferencesUserDataSource(this)
                )
            }

            val mapViewModel: MapViewModel by viewModels {
                MapViewModelFactory(TripRepository(), EventRepository(), SharedPreferencesUserDataSource(this))
            }

            val newSignInViewModel: NewSignInViewModel by viewModels {
                NewSignInViewModelFactory(navigationController, AuthRepo(), SharedPreferencesUserDataSource(this))
            }

            val signUpViewModel: SignUpViewModel by viewModels {
                SignUpViewModelFactory(navigationController, AuthRepo())
            }

            val tripViewModel: TripViewModel by viewModels {
                TripViewModelFactory(TripRepository(), SharedPreferencesUserDataSource(this))
            }

            val archivedTripViewModel: ArchivedTripViewModel by viewModels {
                ArchivedTripViewModelFactory(TripRepository(), SharedPreferencesUserDataSource(this))
            }

            val addTripViewModel: AddTripViewModel by viewModels {
                AddTripViewModelFactory(TripRepository(), SharedPreferencesUserDataSource(this))
            }

            val modifyTripViewModel: ModifyTripViewModel by viewModels {
                ModifyTripViewModelFactory(TripRepository(), SharedPreferencesUserDataSource(this))
            }

            val eventViewModel: EventViewModel by viewModels {
                EventViewModelFactory(EventRepository(), TripRepository(), SharedPreferencesUserDataSource(this))
            }

            val addEventViewModel: AddEventViewModel by viewModels {
                AddEventViewModelFactory(EventRepository(), GooglePredictionRepository(), SharedPreferencesUserDataSource(this))
            }

            val modifyEventViewModel: ModifyEventViewModel by viewModels {
                ModifyEventViewModelFactory(EventRepository(), GooglePredictionRepository(), SharedPreferencesUserDataSource(this))
            }

            val userDataSource = SharedPreferencesUserDataSource(this)
            val user = userDataSource.getUser()
            val isLoggedIn = user != null

            TravelBookTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationView(
                        navController = navigationController,
                        signInViewModel = signInViewModel,
                        mapViewModel = mapViewModel,
                        tripViewModel = tripViewModel,
                        archivedTripViewModel = archivedTripViewModel,
                        addTripViewModel = addTripViewModel,
                        modifyTripViewModel = modifyTripViewModel,
                        eventViewModel = eventViewModel,
                        addEventViewModel = addEventViewModel,
                        modifyEventViewModel = modifyEventViewModel,
                        profileViewModel = profileViewModel,
                        newSignInViewModel = newSignInViewModel,
                        signUpViewModel = signUpViewModel,
                        isLoggedIn = isLoggedIn
                    )
                }
            }
        }
    }
}