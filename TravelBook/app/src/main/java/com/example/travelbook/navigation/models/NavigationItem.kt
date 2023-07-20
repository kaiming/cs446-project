package com.example.travelbook.navigation.models

import com.example.travelbook.R

sealed class NavigationItem(
    val name: String,
    val icon: Int,
    val route: String
) {
    object Map : NavigationItem("Map", R.drawable.baseline_map_24,"map")
    object Trip : NavigationItem("Trip", R.drawable.baseline_airplanemode_active_24, "trip")
    object Event : NavigationItem("Event", R.drawable.logo, "event")
    object Profile : NavigationItem("Profile", R.drawable.baseline_account_circle_24, "profile")
    object SignIn : NavigationItem("Sign In", R.drawable.logo, "signIn")
    object AddTrip : NavigationItem("Add Trip", R.drawable.logo, "addTrip")
    object AddEvent : NavigationItem("Add Event", R.drawable.logo, "addEvent")
    object SignUp : NavigationItem("Sign Up", R.drawable.logo, "signUp")
    object ModifyEvent : NavigationItem("Modify Event", R.drawable.logo, "modifyEvent")
    object BudgetDetail : NavigationItem("Budget Detail", R.drawable.logo, "budgetDetail")
    // TODO: Add other nav paths here

}