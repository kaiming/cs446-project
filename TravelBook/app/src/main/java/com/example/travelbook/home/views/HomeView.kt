package com.example.travelbook.home.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.travelbook.home.viewModels.HomeScreenViewModel
import androidx.compose.ui.Modifier
import com.example.travelbook.home.viewModels.ViewState

@Composable
fun HomeView(
    viewModel: HomeScreenViewModel,
    onNavigateToLoadingScreen: () -> Unit = {},
    onNavigateToLoginScreen: () -> Unit = {},
    onNavigateToHomeScreen: () -> Unit = {},
    modifier: Modifier = Modifier
) {



    when (viewModel.isLoggedIn) {
        false -> {
            LaunchedEffect(viewModel.isLoggedIn) {
                onNavigateToLoginScreen()
            }
        }
        true -> {
            LaunchedEffect(viewModel.isLoggedIn) {
                onNavigateToHomeScreen()
            }
        }
        else -> {
            LaunchedEffect(viewModel.isLoggedIn) {
                onNavigateToLoadingScreen()
            }
        }
    }
}