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
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.signIn.AuthRepo
import com.example.travelbook.signIn.viewModels.NewSignInViewModel
import com.example.travelbook.signIn.viewModels.NewSignInViewModelFactory
import com.example.travelbook.signIn.viewModels.SignUpViewModel
import com.example.travelbook.trips.viewModels.AddTripViewModel
import com.example.travelbook.trips.viewModels.AddTripViewModelFactory
import com.example.travelbook.trips.viewModels.TripViewModel
import com.example.travelbook.trips.viewModels.TripViewModelFactory
import com.example.travelbook.ui.theme.TravelBookTheme
import com.google.android.libraries.places.api.Places

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TEMP MODEL TESTING
        val tripRepo = TripRepository()
        // val eventRepo = EventRepository()
        // Add trip
        // tripRepo.addTrip(trip)
        // Add event
        // eventRepo.addEvent("wyj79g8Ye5ILpHVuqr7i", EventItem("Event 1", "2021-10-01", "2021-10-02", "123 f u. . st"))
        // tripRepo.addUserToTrip("wyj79g8Ye5ILpHVuqr7i", "user2")

        setContent {

            Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)

            val navigationController = rememberNavController()

            val signInViewModel: SignInViewModel by viewModels {
                SignInViewModelFactory(navigationController, AuthRepo())
            }

            val newSignInViewModel: NewSignInViewModel by viewModels {
                NewSignInViewModelFactory(navigationController, AuthRepo(), SharedPreferencesUserDataSource(this))
            }

            val signUpViewModel: SignUpViewModel by viewModels {
                SignUpViewModelFactory(navigationController, AuthRepo())
            }

            val tripViewModel: TripViewModel by viewModels {
                TripViewModelFactory(TripRepository())
            }

            val addTripViewModel: AddTripViewModel by viewModels {
                AddTripViewModelFactory(TripRepository())
            }

            val eventViewModel: EventViewModel by viewModels {
                EventViewModelFactory(EventRepository())
            }

            val addEventViewModel: AddEventViewModel by viewModels {
                AddEventViewModelFactory(EventRepository(), GooglePredictionRepository())
            }

            val modifyEventViewModel: ModifyEventViewModel by viewModels {
                ModifyEventViewModelFactory(EventRepository(), GooglePredictionRepository())
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
                        tripViewModel = tripViewModel,
                        addTripViewModel = addTripViewModel,
                        eventViewModel = eventViewModel,
                        addEventViewModel = addEventViewModel,
                        modifyEventViewModel = modifyEventViewModel,
                        newSignInViewModel = newSignInViewModel,
                        signUpViewModel = signUpViewModel,
                        isLoggedIn = isLoggedIn
                    )
                }
            }
        }
    }
}