package com.example.travelbook.navigation.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.travelbook.R

sealed class NavigationItem(
    val name: String,
    val icon: ImageVector,
    val route: String
) {
    object Map : NavigationItem("Map", Icons.Filled.Home,"map")
    // TODO: Add other nav paths here
}