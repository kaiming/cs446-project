package com.example.travelbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.travelbook.navigation.views.NavigationView
import com.example.travelbook.signIn.viewModels.SignInViewModel
import com.example.travelbook.signIn.viewModels.SignInViewModelFactory
import com.example.travelbook.events.models.EventRepository
import com.example.travelbook.trips.models.TripRepository
import com.example.travelbook.events.viewModels.AddEventViewModel
import com.example.travelbook.events.viewModels.AddEventViewModelFactory
import com.example.travelbook.events.viewModels.EventViewModel
import com.example.travelbook.events.viewModels.EventViewModelFactory
import com.example.travelbook.signIn.SignInRepo
import com.example.travelbook.trips.viewModels.AddTripViewModel
import com.example.travelbook.trips.viewModels.AddTripViewModelFactory
import com.example.travelbook.trips.viewModels.TripViewModel
import com.example.travelbook.trips.viewModels.TripViewModelFactory
import com.example.travelbook.ui.theme.TravelBookTheme

// val trip = createTrip(
//     "Trip with friends", "2021-10-01", "2021-10-10", List(1){"user1"}
// )

private const val TAG = "MainActivity"

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

            val navigationController = rememberNavController()

            val signInViewModel: SignInViewModel by viewModels {
                SignInViewModelFactory(navigationController, SignInRepo())
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
                AddEventViewModelFactory(EventRepository())
            }

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
                        addEventViewModel = addEventViewModel
                    )
                }
            }
        }
    }
}