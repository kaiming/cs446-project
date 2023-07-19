package com.example.travelbook.trips.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.travelbook.trips.models.Trip
import com.example.travelbook.trips.viewModels.TripViewModel
import com.example.travelbook.ui.theme.Padding

@Composable
fun TripView(
    viewModel: TripViewModel,
    onNavigateToEvents: (String, Float) -> Unit,
    onNavigateToArchivedTrip: () -> Unit,
    onNavigateToAddTrip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val trips = viewModel.tripsFlow.collectAsStateWithLifecycle(initialValue = emptyList())
    Box(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Trips",
                fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                fontSize = 32.sp,
                modifier = Modifier.padding(Padding.PaddingSmall.size)
            )
            LazyColumn(Modifier.weight(6f)) {
                items(items = trips.value, itemContent = { trip ->
                    if (!trip.isArchived) {
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

                            Spacer(modifier = Modifier.width(8.dp))

                            ThreeDotMenu(viewModel, {viewModel.archiveTrip(trip)})
                        }
                    }
                })
            }

            Box(modifier = Modifier.fillMaxSize().weight(1f)) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Padding.PaddingMedium.size)
                        .background(
                            color = MaterialTheme.colorScheme.background.copy(alpha = 0f)
                        ),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    IconButton(
                        onClick = {
                            onNavigateToAddTrip()
                        },
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            Icons.Rounded.AddCircle,
                            tint = MaterialTheme.colorScheme.secondary,
                            contentDescription = "Add Trip Button",
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomStart
                ) {
                    TextButton(onClick = { onNavigateToArchivedTrip() }) {
                        Text(text = "Archived Trips")
                    }
                }
            }
        }
    }
}

@Composable
fun ThreeDotMenu(
    viewModel: TripViewModel,
    onArchiveClick: () -> Unit
) {
    val context = LocalContext.current
    val expandedState = remember { mutableStateOf(false) }

    Box(contentAlignment = Alignment.Center) {
        IconButton(
            onClick = { expandedState.value = true }
        ) {
            Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = "Menu")
        }

        DropdownMenu(
            expanded = expandedState.value,
            onDismissRequest = { expandedState.value = false }
        ) {
            DropdownMenuItem(onClick = {
                onArchiveClick()
                expandedState.value = false
            }) {
                Text("Archive")
            }
            DropdownMenuItem(onClick = { /* Handle edit click */ }) {
                Text("Edit")
            }
            DropdownMenuItem(onClick = { /* Handle delete click */ }) {
                Text("Delete")
            }
        }
    }
}

@Composable
public fun TripCard(
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