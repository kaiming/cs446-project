package com.example.travelbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.travelbook.map.views.MapView
import com.example.travelbook.navigation.views.NavigationView
import com.example.travelbook.signIn.viewModels.SignInViewModel
import com.example.travelbook.signIn.viewModels.SignInViewModelFactory
import com.example.travelbook.navigation.views.NavigationView
import com.example.travelbook.trips.models.EventItem
import com.example.travelbook.trips.models.EventRepository
import com.example.travelbook.trips.models.TripRepository
import com.example.travelbook.trips.models.createTrip
import com.example.travelbook.trips.viewModels.AddEventViewModel
import com.example.travelbook.trips.viewModels.AddEventViewModelFactory
import com.example.travelbook.trips.viewModels.AddTripViewModel
import com.example.travelbook.trips.viewModels.AddTripViewModelFactory
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
        // val tripRepo = TripRepository()
        // val eventRepo = EventRepository()
        // Add trip
        // tripRepo.addTrip(trip)
        // Add event
        // eventRepo.addEvent("wyj79g8Ye5ILpHVuqr7i", EventItem("Event 1", "2021-10-01", "2021-10-02", "123 f u. . st"))
        // tripRepo.addUserToTrip("wyj79g8Ye5ILpHVuqr7i", "user2")

        setContent {

            val navigationController = rememberNavController()
            val signInViewModel: SignInViewModel by viewModels {
                SignInViewModelFactory(navigationController)
            }

            val addTripViewModelFactory = AddTripViewModelFactory(TripRepository())
            val addTripViewModel = ViewModelProvider(this, addTripViewModelFactory).get(AddTripViewModel::class.java)

            val addEventViewModelFactory = AddEventViewModelFactory(EventRepository())
            val addEventViewMode = ViewModelProvider(this, addEventViewModelFactory).get(AddEventViewModel::class.java)

            TravelBookTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationView(
                        navigationController,
                        signInViewModel
                    )
                }
            }
        }
    }
}