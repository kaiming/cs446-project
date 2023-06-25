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
import com.example.travelbook.home.viewModels.HomeScreenViewModel
import com.example.travelbook.home.viewModels.HomeScreenViewModelFactory
import com.example.travelbook.navigation.views.NavigationView
import com.example.travelbook.signIn.viewModels.SignInStateModelFactory
import com.example.travelbook.signIn.viewModels.SignInViewModel
import com.example.travelbook.ui.theme.TravelBookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            val signInViewModel: SignInViewModel by viewModels {
                SignInStateModelFactory((application as TravelBookApplication).signInStateRepository)
            }

            val homeScreenViewModel: HomeScreenViewModel by viewModels {
                HomeScreenViewModelFactory((application as TravelBookApplication).signInStateRepository)
            }

            TravelBookTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationView(
                        navController,
                        signInViewModel,
                        homeScreenViewModel
                    )
                }
            }
        }
    }
}