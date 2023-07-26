package com.example.travelbook.navigation.views

import android.content.res.Resources
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.twotone.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.travelbook.R
import com.example.travelbook.budgeting.viewModels.BudgetingViewModel
import com.example.travelbook.budgeting.views.BudgetingView
import com.example.travelbook.events.viewModels.AddEventViewModel
import com.example.travelbook.events.viewModels.EventViewModel
import com.example.travelbook.events.viewModels.ModifyEventViewModel
import com.example.travelbook.events.views.AddEventView
import com.example.travelbook.events.views.AddTripView
import com.example.travelbook.events.views.EventView
import com.example.travelbook.events.views.ModifyEventView
import com.example.travelbook.map.viewModels.MapViewModel
import com.example.travelbook.map.views.MapView
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.profile.viewModels.ProfileViewModel
import com.example.travelbook.profile.views.ProfileView
import com.example.travelbook.shared.UIText
import com.example.travelbook.signIn.viewModels.NewSignInViewModel
import com.example.travelbook.signIn.viewModels.SignInViewModel
import com.example.travelbook.signIn.viewModels.SignUpViewModel
import com.example.travelbook.signIn.views.NewSignInView
import com.example.travelbook.signIn.views.SignInView
import com.example.travelbook.signIn.views.SignUpView
import com.example.travelbook.trips.viewModels.AddTripViewModel
import com.example.travelbook.trips.viewModels.ArchivedTripViewModel
import com.example.travelbook.trips.viewModels.TripViewModel
import com.example.travelbook.trips.views.ArchivedTripView
import com.example.travelbook.trips.viewModels.ModifyTripViewModel
import com.example.travelbook.trips.views.ModifyTripView
import com.example.travelbook.trips.views.TripView
import com.example.travelbook.ui.theme.Padding
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationView(
    navController: NavHostController,
    signInViewModel: SignInViewModel,
    mapViewModel: MapViewModel,
    tripViewModel: TripViewModel,
    archivedTripViewModel: ArchivedTripViewModel,
    addTripViewModel: AddTripViewModel,
    modifyTripViewModel: ModifyTripViewModel,
    eventViewModel: EventViewModel,
    addEventViewModel: AddEventViewModel,
    modifyEventViewModel: ModifyEventViewModel,
    profileViewModel: ProfileViewModel,
    newSignInViewModel: NewSignInViewModel,
    signUpViewModel: SignUpViewModel,
    budgetingViewModel: BudgetingViewModel,
    isLoggedIn: Boolean,
    modifier: Modifier = Modifier
) {

    val startDestination = if (isLoggedIn) NavigationItem.Map.route else NavigationItem.SignIn.route
    val navigationItems = listOf(
        NavigationItem.Map,
        NavigationItem.Trip,
        NavigationItem.Profile
    )
    val selectedItem = remember { mutableStateOf(navigationItems[0]) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val shouldShowNavBar = navigationItems.any {
        it.route == (navBackStackEntry?.destination?.route ?: false)
    }

    Scaffold(
        bottomBar = {
            if (shouldShowNavBar) {
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
            }
        },
    ) {
        NavigationGraph(
            navController = navController,
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
            startDestination = startDestination,
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
    mapViewModel: MapViewModel,
    tripViewModel: TripViewModel,
    archivedTripViewModel: ArchivedTripViewModel,
    addTripViewModel: AddTripViewModel,
    modifyTripViewModel: ModifyTripViewModel,
    eventViewModel: EventViewModel,
    addEventViewModel: AddEventViewModel,
    modifyEventViewModel: ModifyEventViewModel,
    profileViewModel: ProfileViewModel,
    newSignInViewModel: NewSignInViewModel,
    signUpViewModel: SignUpViewModel,
    budgetingViewModel: BudgetingViewModel,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(navController, startDestination = startDestination) {
        composable(NavigationItem.Map.route) {
            MapView(
                viewModel = mapViewModel,
                modifier = modifier
            )
        }
        composable(NavigationItem.Trip.route) {
            TripView(
                viewModel = tripViewModel,
                onNavigateToAddTrip = {
                    navController.navigate(NavigationItem.AddTrip.route)
                },
                onNavigateToModifyTrip = {
                    navController.navigate("${NavigationItem.ModifyTrip.route}/$it")
                },
                onNavigateToArchivedTrip = {navController.navigate(NavigationItem.ArchivedTrip.route)},
                onNavigateToEvents = {
                    navController.navigate("${NavigationItem.Event.route}/$it")
                },
                modifier = modifier
            )
        }
        composable(NavigationItem.ArchivedTrip.route) {
            ArchivedTripView(
                viewModel = archivedTripViewModel,
                onNavigateToEvents = { eventString ->
                    navController.navigate("${NavigationItem.Event.route}/$eventString")
                },

                modifier = modifier
            )
        }
        composable(NavigationItem.AddTrip.route) {
            AddTripView(
                viewModel = addTripViewModel,
                onNavigateToTrip = {
                    navController.popBackStack()
                },
                modifier = modifier
            )
        }
        composable(
            route = "${NavigationItem.ModifyTrip.route}/{trip_id}",
            arguments = listOf(
                navArgument("trip_id") {
                    type = NavType.StringType
                }
            )
        ) {
            ModifyTripView(
                viewModel = modifyTripViewModel,
                tripId = it.arguments?.getString("trip_id"),
                onNavigateToEvents = {
                    navController.popBackStack()
                },
                modifier = modifier
            )
        }
        composable(
            route = "${NavigationItem.Event.route}/{trip_id}",
            arguments = listOf(
                navArgument("trip_id") {
                    type = NavType.StringType
                }
            )
        ) {
            EventView(
                viewModel = eventViewModel,
                tripId = it.arguments?.getString("trip_id"),
                onNavigateToAddEvent = {
                    navController.navigate("${NavigationItem.AddEvent.route}/$it")
                },
                onNavigateToModifyEvent = { tripId, eventId ->
                    navController.navigate("${NavigationItem.ModifyEvent.route}/$tripId/$eventId")
                },
                onNavigateToBudgetDetails = {
                    navController.navigate("${NavigationItem.BudgetDetail.route}/$it")
                },
                modifier = modifier
            )
        }
        composable(
            route = "${NavigationItem.AddEvent.route}/{trip_id}",
            arguments = listOf(
                navArgument("trip_id") {
                    type = NavType.StringType
                }
            )
        ) {
            AddEventView(
                viewModel = addEventViewModel,
                tripId = it.arguments?.getString("trip_id"),
                onNavigateToEvents = {
                    navController.popBackStack()
                },
                modifier = modifier
            )
        }
        composable(
            route = "${NavigationItem.ModifyEvent.route}/{trip_id}/{event_id}",
            arguments = listOf(
                navArgument("trip_id") {
                    type = NavType.StringType
                },
                navArgument("event_id") {
                    type = NavType.StringType
                }
            )
        ) {
            ModifyEventView(
                viewModel = modifyEventViewModel,
                tripId = it.arguments?.getString("trip_id"),
                eventId = it.arguments?.getString("event_id"),
                onNavigateToEvents = {
                    navController.popBackStack()
                },
                modifier = modifier
            )
        }
        composable(
            route = "${NavigationItem.BudgetDetail.route}/{trip_id}",
            arguments = listOf(
                navArgument("trip_id") {
                    type = NavType.StringType
                }
            )
        ) {
            BudgetingView(
                viewModel = budgetingViewModel,
                tripId = it.arguments?.getString("trip_id"),
                onNavigateToBudgetDetails = {
                    navController.popBackStack()
                },
                modifier = modifier
            )
        }
        composable(NavigationItem.Profile.route) {
            ProfileView(profileViewModel)
        }
        composable(NavigationItem.SignIn.route) {
            NewSignInView(newSignInViewModel)
        }
        composable(NavigationItem.SignUp.route) {
            SignUpView(signUpViewModel)
        }
    }
}