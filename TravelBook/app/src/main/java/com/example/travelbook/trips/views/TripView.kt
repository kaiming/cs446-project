package com.example.travelbook.trips.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.travelbook.trips.models.Trip
import com.example.travelbook.trips.viewModels.TripViewModel
import com.example.travelbook.ui.theme.Padding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TripView(
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    val trips: List<Trip> by viewModel.trips.collectAsStateWithLifecycle(initialValue = emptyList())


}


@Composable
private fun TripCard(
    trip: Trip,
    viewModel: TripViewModel
) {
    Card(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier.padding(Padding.PaddingMedium.size)
    ) {
        Row(modifier = Modifier.padding(Padding.PaddingExtraLarge.size)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = trip.tripName,
                )
                Text(
                    text = trip.startDate,
                )
                Text(
                    text = trip.endDate,
                )
                Text(
                    text = trip.participants.toString(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                // space for image/trip logo... don't think the model supports this yet.
            }
        }
    }
}