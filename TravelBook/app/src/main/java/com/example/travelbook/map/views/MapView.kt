package com.example.travelbook.map.views

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings

@Composable
fun MapView(
    // TODO: Set up viewmodel and model for the map view
    modifier: Modifier = Modifier
) {
    val uiSettings = remember { mutableStateOf(
        MapUiSettings(
            compassEnabled = true,
            zoomControlsEnabled = false
        )
    ) }

    Box(
        modifier = modifier
    ) {
        GoogleMap(
            uiSettings = uiSettings.value
        ) {

        }
    }

}