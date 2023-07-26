package com.example.travelbook.trips.views

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.travelbook.R
import com.example.travelbook.trips.models.Trip
import com.example.travelbook.trips.viewModels.TripViewModel
import com.example.travelbook.ui.theme.Padding

@Composable
fun TripView(
    viewModel: TripViewModel,
    onNavigateToEvents: (String) -> Unit,
    onNavigateToArchivedTrip: () -> Unit,
    onNavigateToAddTrip: () -> Unit,
    onNavigateToModifyTrip: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        viewModel.loadTrips()
    }
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
                items(items = trips.value.sortedBy { it.startDate }, itemContent = { trip ->
                    if (!trip.isArchived) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = { onNavigateToEvents(trip.tripId) },
                                modifier = Modifier.weight(1f)
                            ) {
                                TripCard(
                                    trip,
                                    { viewModel.archiveTrip(trip.tripId) },
                                    { onNavigateToModifyTrip(trip.tripId) },
                                    { viewModel.deleteTrip(trip.tripId) }
                                )
                            }

                        }
                    }
                })
            }

            Box(modifier = Modifier
                .fillMaxSize()
                .weight(1f)) {
                FloatingAddButton(
                    onNavigateToAddTrip = { onNavigateToAddTrip() }
                )

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomStart
                ) {
                        TextButton(onClick = { onNavigateToArchivedTrip() }) {
                            Icon(
                                Icons.Rounded.Lock,
                                contentDescription = "Archive",
                            )
                            Text(text = "Archived Trips")
                        }
                }
            }
        }
    }
}

@Composable
fun FloatingAddButton(
    onNavigateToAddTrip: () -> Unit
) {
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
}

@Composable
fun ThreeDotMenu(
    viewModel: TripViewModel,
    onArchiveClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
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
            DropdownMenuItem(onClick = { onEditClick() }) {
                Text("Edit")
            }
            DropdownMenuItem(onClick = { onDeleteClick() }) {
                Text("Delete")
            }
        }
    }
}

@Composable
fun TripCard(
    trip: Trip,
    onArchiveClick: (() -> Unit)?,
    onEditClick: (() -> Unit)?,
    onDeleteClick: (() -> Unit)?
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier
            .padding(Padding.PaddingExtraSmall.size)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(0xFFECECEC))
                )
            ),
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(Padding.PaddingLarge.size)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Padding.PaddingSmall.size)
            ) {
                Text(
                    text = trip.tripName,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (onEditClick != null) {
                        Icon(
                            Icons.Rounded.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { onEditClick() },
                        )
                    }

                    if (onArchiveClick != null) {
                        Icon(
                            Icons.Rounded.Lock,
                            contentDescription = "Archive",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { onArchiveClick() }
                        )
                    }

                    if (onDeleteClick != null) {
                        Icon(
                            Icons.Rounded.Close,
                            contentDescription = "Delete",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { onDeleteClick() }
                        )
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = "Start Date",
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = trip.startDate + " to " + trip.endDate,
                    maxLines = 1,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Gray,
                        fontStyle = FontStyle.Italic
                    ),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                trip.participants.take(3).forEach { participant ->
                    val initials = convertToInitials(participant)

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.Gray, RoundedCornerShape(50))
                            .clip(RoundedCornerShape(50))
                    ) {
                        Text(
                            text = initials,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                if (trip.participants.size > 3) {
                    Text(
                        text = "+${trip.participants.size - 3}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

fun convertToInitials(name: String): String {
    val delimitedNames = name.split(" ")
    var initials = ""

    for (delimitedName in delimitedNames) {
        initials += delimitedName[0].uppercaseChar()
    }

    return initials
}