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
import com.example.travelbook.budgeting.viewModels.BudgetingViewModel
import com.example.travelbook.budgeting.viewModels.BudgetingViewModelFactory
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
import com.example.travelbook.photos.models.PhotosRepository
import com.example.travelbook.photos.viewModels.PhotosViewModel
import com.example.travelbook.photos.viewModels.PhotosViewModelFactory
import com.example.travelbook.profile.viewModels.ProfileViewModel
import com.example.travelbook.profile.viewModels.ProfileViewModelFactory
import com.example.travelbook.signIn.AuthRepo
import com.example.travelbook.signIn.viewModels.NewSignInViewModel
import com.example.travelbook.signIn.viewModels.NewSignInViewModelFactory
import com.example.travelbook.signIn.viewModels.SignUpViewModel
import com.example.travelbook.travelAdvisory.models.TravelAdvisoryRepository
import com.example.travelbook.travelAdvisory.viewModels.TravelAdvisoryViewModel
import com.example.travelbook.travelAdvisory.viewModels.TravelAdvisoryViewModelFactory
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

        val userDataSource = SharedPreferencesUserDataSource.getInstance(this)
        val tripRepo = TripRepository.getInstance()
        val eventRepo = EventRepository.getInstance()

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
                    userDataSource
                )
            }

            val mapViewModel: MapViewModel by viewModels {
                MapViewModelFactory(tripRepo, eventRepo, userDataSource)
            }

            val newSignInViewModel: NewSignInViewModel by viewModels {
                NewSignInViewModelFactory(navigationController, AuthRepo(), userDataSource)
            }

            val signUpViewModel: SignUpViewModel by viewModels {
                SignUpViewModelFactory(navigationController, AuthRepo())
            }

            val tripViewModel: TripViewModel by viewModels {
                TripViewModelFactory(tripRepo, userDataSource)
            }

            val archivedTripViewModel: ArchivedTripViewModel by viewModels {
                ArchivedTripViewModelFactory(tripRepo, userDataSource)
            }

            val addTripViewModel: AddTripViewModel by viewModels {
                AddTripViewModelFactory(tripRepo, userDataSource)
            }

            val modifyTripViewModel: ModifyTripViewModel by viewModels {
                ModifyTripViewModelFactory(tripRepo, userDataSource)
            }

            val eventViewModel: EventViewModel by viewModels {
                EventViewModelFactory(eventRepo, tripRepo, userDataSource, navigationController)
            }

            val addEventViewModel: AddEventViewModel by viewModels {
                AddEventViewModelFactory(eventRepo, tripRepo, GooglePredictionRepository(), userDataSource)
            }

            val modifyEventViewModel: ModifyEventViewModel by viewModels {
                ModifyEventViewModelFactory(eventRepo, tripRepo, GooglePredictionRepository(), userDataSource)
            }

            val budgetingViewModel: BudgetingViewModel by viewModels {
                BudgetingViewModelFactory(tripRepo, eventRepo);
            }

            val photosViewModel: PhotosViewModel by viewModels {
                PhotosViewModelFactory(PhotosRepository(), userDataSource)
            }

            val travelAdvisoryViewModel: TravelAdvisoryViewModel by viewModels {
                TravelAdvisoryViewModelFactory(eventRepo, TravelAdvisoryRepository())
            }

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
                        budgetingViewModel = budgetingViewModel,
                        photosViewModel = photosViewModel,
                        travelAdvisoryViewModel = travelAdvisoryViewModel,
                        isLoggedIn = isLoggedIn
                    )
                }
            }
        }
    }
}