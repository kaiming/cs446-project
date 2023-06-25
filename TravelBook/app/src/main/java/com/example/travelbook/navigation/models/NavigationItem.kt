package com.example.travelbook.navigation.models

import com.example.travelbook.R

sealed class NavigationItem(
    val name: String,
    val icon: Int,
    val route: String
) {
    object Map : NavigationItem("Map", R.drawable.baseline_map_24,"map")
    object Trip : NavigationItem("Trip", R.drawable.baseline_airplanemode_active_24, "trip")
    object Profile : NavigationItem("Profile", R.drawable.baseline_account_circle_24, "profile")
    object SignUp : NavigationItem("Sign Up", R.drawable.baseline_account_circle_24, "signUp")
    object Home : NavigationItem("Home", R.drawable.baseline_home_24, "home")
    // TODO: Add other nav paths here
}