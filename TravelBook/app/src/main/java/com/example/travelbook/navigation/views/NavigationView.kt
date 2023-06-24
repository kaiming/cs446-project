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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.travelbook.R
import com.example.travelbook.map.views.MapView
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.shared.UIText
import com.example.travelbook.ui.theme.Padding
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationView(
    modifier: Modifier = Modifier
) {
    val navigationItems = listOf(
        NavigationItem.Map,
        NavigationItem.Trip,
        NavigationItem.Profile
    )
    val navController = rememberNavController()
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
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        )
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(navController, startDestination = NavigationItem.Map.route) {
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
    }
}