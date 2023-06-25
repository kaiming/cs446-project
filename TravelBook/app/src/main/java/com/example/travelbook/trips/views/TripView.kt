package com.example.travelbook.trips.views

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.travelbook.trips.models.Trip
import com.example.travelbook.trips.viewModels.TripViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TripView(
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    val trips: List<Trip> by viewModel.trips.collectAsStateWithLifecycle(initialValue = emptyList())
}