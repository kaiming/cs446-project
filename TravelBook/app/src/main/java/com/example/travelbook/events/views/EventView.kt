package com.example.travelbook.events.views

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.events.viewModels.EventViewModel
import com.example.travelbook.trips.views.TripCard
import com.example.travelbook.ui.theme.Padding
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EventView(
    viewModel: EventViewModel,
    tripId: String?,
    onNavigateToAddEvent: (String) -> Unit,
    onNavigateToModifyEvent: (String, String) -> Unit,
    onNavigateToBudgetDetails: (String) -> Unit,
    onNavigateToTravelAdvisory: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (tripId !is String) return

    val trip = viewModel.getTripByTripId(tripId).collectAsState(null).value ?: return
    val events = viewModel.getEventsFlowByTripId(tripId)
        .collectAsStateWithLifecycle(initialValue = emptyList())

    var totalCosts = 0f
    for (event in events.value) {
        totalCosts += event.cost.toFloat()
    }

    val pickImagesLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            // Handle the returned Uri, e.g., upload to Firebase Storage
            viewModel.handleImageUpload(uri, tripId)
        }
    }

    // photos permission state
    val photosPermissionState = rememberPermissionState(
        android.Manifest.permission.READ_MEDIA_IMAGES
    )

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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp) // Add horizontal padding
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Trip Budget:",
                        fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(Padding.PaddingSmall.size)
                    )
                    Text(
                        text = "$ " + trip.budget,
                        fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(Padding.PaddingSmall.size)
                    )
                }
            }
            BudgetProgressBar(currentBudget = totalCosts, totalBudget = trip.budget.toFloat(), onClick = { onNavigateToBudgetDetails(tripId) })
            Button(
                onClick = {
                    onNavigateToTravelAdvisory(tripId)
                }
            ) {
                Text("View Travel Advisory")
            }
            LazyColumn(Modifier.weight(5.8f)) {
                items(items = events.value, itemContent = { event ->
                    EventCard(event) {
                        onNavigateToModifyEvent(tripId, event.eventId)
                    }
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // "View Photos" Button
                    Button(
                        onClick = {
                            viewModel.navigateToPhotos()
                        },
                        modifier = Modifier.padding(end = Padding.PaddingMedium.size)
                    ) {
                        Text("View Photos")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // "Add Photos" Button
                    Button(
                        onClick = {
                            if (photosPermissionState.status.isGranted) {
                                Log.d("DEBUG", "Permissions granted!")
                                pickImagesLauncher.launch("image/*")
                            } else {
                                Log.d("DEBUG", "Permissions not granted!")
                                photosPermissionState.launchPermissionRequest()
                            }
                        },
                        modifier = Modifier.padding(end = Padding.PaddingMedium.size)
                    ) {
                        Text("Add Photos")
                    }

                    // Spacer to provide some separation
                    Spacer(modifier = Modifier.width(16.dp))

                    // Existing IconButton for "Add Event"
                    IconButton(
                        onClick = {
                            onNavigateToAddEvent(tripId)
                        },
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            Icons.Rounded.AddCircle,
                            tint = MaterialTheme.colorScheme.secondary,
                            contentDescription = "Add Event Button",
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun EventCard(
    event: EventItem,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(15.dp),
        elevation = 4.dp,
        modifier = Modifier
            .padding(Padding.PaddingMedium.size)
            .clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(Padding.PaddingSmall.size)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.name,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black),
                )
                Text(
                    text = event.location,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Gray,
                        fontStyle = FontStyle.Italic
                    ),
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.DateRange,
                        contentDescription = "Start Date",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = event.startDate + " to " + event.endDate,
                        maxLines = 1,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.Gray,
                            fontStyle = FontStyle.Italic
                        ),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Text(
                    text = "$" + event.cost,
                )
            }
        }
    }
}

@Composable
fun BudgetProgressBar(currentBudget: Float, totalBudget: Float, onClick: () -> Unit ) {
    val progress = (currentBudget / totalBudget)
    val restrictedProgress = (currentBudget / totalBudget).coerceIn(0f, 1f)
    // Calculate the progress percentage
    val percentageUsed = (progress * 100).toInt()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp) // Add horizontal padding
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            LinearProgressIndicator(
                progress = restrictedProgress,
                color = when {
                    percentageUsed > 100 -> Color.Red
                    else -> Color.Black
                },
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .padding(end = 4.dp)
            )

            Text(
                text = "$percentageUsed%",
                color = when {
                    percentageUsed > 100 -> Color.Red
                    else -> Color.Black
                },
                fontSize = 16.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddUserPopup(
    onClosePopup: () -> Unit,
    onAddUser: (String) -> Unit,
) {
    val usernameTextFieldValue = remember { mutableStateOf(TextFieldValue()) }
    AlertDialog(
        onDismissRequest = onClosePopup,
        title = { Text(text = "Add User") },
        text = {
            TextField(
                value = usernameTextFieldValue.value,
                onValueChange = { usernameTextFieldValue.value = it },
                label = { Text(text = "Email") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onAddUser(usernameTextFieldValue.value.text) }
                )
            )
        },
        confirmButton = {
            OutlinedButton(onClick = { onAddUser(usernameTextFieldValue.value.text) }) {
                Text(text = "Add")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onClosePopup) {
                Text(text = "Cancel")
            }
        }
    )
}