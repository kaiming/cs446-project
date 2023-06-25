package com.example.travelbook.navigation.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.travelbook.home.viewModels.HomeScreenViewModel
import com.example.travelbook.home.views.HomeView
import com.example.travelbook.map.views.MapView
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.signIn.viewModels.SignInViewModel
import com.example.travelbook.signIn.views.SignInView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationView(
    navController: NavHostController,
    signInViewModel: SignInViewModel,
    homeScreenViewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier
) {
    val navigationItems = listOf(
        NavigationItem.Map,
        NavigationItem.Trip,
        NavigationItem.Profile
    )
    val selectedItem = remember { mutableStateOf(navigationItems[0]) }

    Scaffold(
        bottomBar = {
            BottomNavigation(
                backgroundColor = MaterialTheme.colorScheme.background
            ) {
                navigationItems.forEach { item ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                ImageVector.vectorResource(id = item.icon),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        label = { Text(item.name) },
                        selected = item == selectedItem.value,
                        onClick = {
                            selectedItem.value = item
                            navController.navigate(item.route)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        // TODO: Add top bar back in if needed
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = {
//                    Row(
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(
//                            text = UIText.ResourceString(R.string.app_name).getString(),
//                            style = MaterialTheme.typography.titleLarge,
//                            color = MaterialTheme.colorScheme.primary
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
//                    containerColor = Color.Transparent
//                )
//            )
//        }
    ) {
        NavigationGraph(
            navController = navController,
            signInViewModel = signInViewModel,
            homeScreenViewModel = homeScreenViewModel,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        )
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    signInViewModel: SignInViewModel,
    homeScreenViewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) {
            HomeView(
                viewModel = homeScreenViewModel,
                onNavigateToLoadingScreen = {
                    navController.navigate(NavigationItem.SignUp.route)
                },
                onNavigateToLoginScreen = {
                    navController.navigate(NavigationItem.SignUp.route)
                },
                onNavigateToHomeScreen = {
                    navController.navigate(NavigationItem.Map.route)
                },
                modifier = modifier
            )
        }
        composable(NavigationItem.Map.route) {
            MapView(
                modifier = modifier
            )
        }
        composable(NavigationItem.Trip.route) {
            MapView(
                modifier = modifier
            )
        }
        composable(NavigationItem.Profile.route) {
            MapView(
                modifier = modifier
            )
        }
        composable(NavigationItem.SignUp.route) {
            SignInView(
                viewModel = signInViewModel,
                modifier = modifier
            )
        }
    }
}