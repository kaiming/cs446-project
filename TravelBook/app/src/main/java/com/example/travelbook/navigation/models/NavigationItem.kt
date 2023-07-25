package com.example.travelbook.navigation.models

import androidx.navigation.Navigation
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
    object ModifyTrip : NavigationItem("Modify Trip", R.drawable.logo, "modifyTrip")
    object AddEvent : NavigationItem("Add Event", R.drawable.logo, "addEvent")
    object SignUp : NavigationItem("Sign Up", R.drawable.logo, "signUp")
    object ModifyEvent : NavigationItem("Modify Event", R.drawable.logo, "modifyEvent")
    object ArchivedTrip : NavigationItem("Archived Trip", R.drawable.logo, "archivedTrip")
    // TODO: Add other nav paths here
    object Budgeting : NavigationItem("temp budgeting", R.drawable.logo, "budget temp")

}