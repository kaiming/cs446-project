package com.example.travelbook.events.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.events.viewModels.EventViewModel
import com.example.travelbook.ui.theme.Padding

@Composable
fun EventView(
    viewModel: EventViewModel,
    tripId: String?,
    onNavigateToAddEvent: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (tripId !is String) return

    val events = viewModel.getEventsFlowByTripId(tripId).collectAsStateWithLifecycle(initialValue = emptyList())
    Box(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Itinerary",
                fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                fontSize = 32.sp,
                modifier = Modifier.padding(Padding.PaddingSmall.size)
            )
            LazyColumn(Modifier.weight(6f)) {
                items(items = events.value, itemContent = { event ->
                    EventCard(event)
                })
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(Padding.PaddingMedium.size)
                    .background(
                        color = MaterialTheme.colorScheme.background.copy(alpha = 0f)
                    ),
                contentAlignment = Alignment.BottomEnd,
            ) {
                IconButton(
                    onClick = {
                        onNavigateToAddEvent(tripId)
                    },
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        Icons.Rounded.AddCircle,
                        tint =  MaterialTheme.colorScheme.secondary,
                        contentDescription = "Add Event Button",
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
        }
    }
}


@Composable
private fun EventCard(
    event: EventItem
) {
    Card(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier.padding(Padding.PaddingMedium.size)
    ) {
        Row(modifier = Modifier.padding(Padding.PaddingExtraLarge.size)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.name,
                )
                Text(
                    text = event.location,
                )
                Text(
                    text = event.date,
                )
                Text(
                    text = event.startTime,
                )
                Text(
                    text = event.endTime,
                )
                Text(
                    text = event.cost,
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                // space for image/event logo... don't think the model supports this yet.
            }
        }
    }
}