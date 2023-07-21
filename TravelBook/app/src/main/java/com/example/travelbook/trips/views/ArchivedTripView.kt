package com.example.travelbook.trips.views

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.travelbook.trips.models.Trip
import com.example.travelbook.trips.viewModels.ArchivedTripViewModel
import com.example.travelbook.ui.theme.Padding

@Composable
fun ArchivedTripView(
    viewModel: ArchivedTripViewModel,
    onNavigateToEvents: (String, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val archivedTrips = viewModel.archivedTripsFlow.collectAsStateWithLifecycle(initialValue = emptyList())
    Log.d("archived trips", "Total: ${archivedTrips.value}") // TEST

    Box(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Archived Trips",
                fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                fontSize = 32.sp,
                modifier = Modifier.padding(Padding.PaddingSmall.size)
            )
            LazyColumn(Modifier.weight(6f)) {
                items(items = archivedTrips.value, itemContent = { trip ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { onNavigateToEvents(trip.tripId, trip.budget.toFloat()) },
                            modifier = Modifier.weight(1f)
                        ) {
                            TripCard(trip)
                        }
                    }
                })
            }
        }
    }
}


@Composable
private fun TripCard(
    trip: Trip
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
