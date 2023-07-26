package com.example.travelbook.events.views

import UtilityFunctions.getTripAsText
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.events.viewModels.EventViewModel
import com.example.travelbook.trips.models.Trip
import com.example.travelbook.trips.views.TripCard
import com.example.travelbook.ui.theme.Padding
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EventView(
    viewModel: EventViewModel,
    tripId: String?,
    onNavigateToAddEvent: (String) -> Unit,
    onNavigateToModifyEvent: (String, String) -> Unit,
    onNavigateToBudgetDetails: (String) -> Unit,
    onNavigateToTravelAdvisory: (String) -> Unit,
    onNavigateToPhotos: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (tripId !is String) return

    val context = LocalContext.current

    val trip = viewModel.getTripByTripId(tripId).collectAsState(null).value ?: return
    val events = viewModel.getEventsFlowByTripId(tripId)
        .collectAsStateWithLifecycle(initialValue = emptyList())

    var totalCosts = 0f
    for (event in events.value) {
        totalCosts += event.cost.toFloat()
    }

    val showAddUserPopup = remember { mutableStateOf(false) }

    val pickImagesLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
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
            BudgetProgressBar(
                currentBudget = totalCosts,
                totalBudget = trip.budget.toFloat(),
                onClick = { onNavigateToBudgetDetails(tripId) })
            Box(
                modifier = Modifier
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
                    Button(
                        onClick = {
                            onNavigateToTravelAdvisory(tripId)
                        }
                    ) {
                        Text("View Travel Advisory")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    // Add user to trip button
                    Button(
                        onClick = { showAddUserPopup.value = true },
                    ) {
                        Text(text = "Add User")
                    }

                    if (showAddUserPopup.value) {
                        AddUserPopup(
                            onClosePopup = { showAddUserPopup.value = false },
                            onAddUser = { email ->
                                viewModel.viewModelScope.launch {
                                    try {
                                        val ret = viewModel.addUserToTrip(tripId, email)
                                        if (ret) {
                                            Toast.makeText(context, "User added successfully!", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "User not found!", Toast.LENGTH_SHORT).show()
                                        }
                                    } catch (e: Exception) {
                                        // Handle exceptions if needed
                                        Toast.makeText(context, "Failed to add user: ${e.message}", Toast.LENGTH_SHORT).show()
                                    } finally {
                                        showAddUserPopup.value = false
                                    }
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Export trip button
                    val view = LocalView.current
                    val context = LocalContext.current
                    IconButton(
                        onClick = {  
                            val bmp = composeViewToBitmap(view)
                            val uri = saveBitmapAsImage(context, bmp)
                            Log.d("DEBUG", "URI: $uri")
                            uri?.let {
                                val shareIntent = Intent(Intent.ACTION_SEND)
                                shareIntent.type = "image/*"
                                shareIntent.putExtra(Intent.EXTRA_STREAM, it)
                                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                                val chooserIntent = Intent.createChooser(shareIntent, "Share Image")
                                context.startActivity(chooserIntent)
                            } ?: run {
                                // Handle case when the image was not saved properly
                                Toast.makeText(context, "Failed to save and share image", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Itinerary Button",
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    // Export trip button
                    IconButton(
                        onClick = { shareAsText(trip, events.value, context) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Share Itinerary Button",
                            modifier = Modifier.size(32.dp)
                        )
                    }

                }
            }
            LazyColumn(Modifier.weight(5.8f)) {
                items(items = events.value.sortedBy { it.startDate }, itemContent = { event ->
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
                            onNavigateToPhotos(tripId)
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
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
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
fun BudgetProgressBar(currentBudget: Float, totalBudget: Float, onClick: () -> Unit) {
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


private fun composeViewToBitmap(view: View): Bitmap {
    return view.let { v ->
        if (v is ComposeView) {
            Log.d("DEBUG", "Capturing ComposeView as bitmap...")
            // Use drawToBitmap extension function to capture the view as a bitmap
            v.drawToBitmap().asImageBitmap().asAndroidBitmap()
        } else {
            Log.d("DEBUG", "Capturing View as bitmap...")
            val bitmap = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            v.draw(canvas)
            bitmap
        }
    }
}

fun saveBitmapAsImage(context: Context, bitmap: Bitmap): Uri? {
    val directory = context.filesDir // Get the app's internal storage directory
    val timestamp = System.currentTimeMillis()
    val filename = "TravelBook-Itinerary-$timestamp.png"
    val file = File(directory, filename)

    Log.d("DEBUG", "Saving screenshot to ${file.absolutePath}")

    try {
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 85, stream)
        stream.flush()
        stream.close()
        Log.d("DEBUG", "Screenshot saved successfully!")

        // Get the content URI using FileProvider
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

        return uri
    } catch (e: Exception) {
        e.printStackTrace()
        Log.d("DEBUG", "Failed to save screenshot!")
        return null
    }
}


private fun shareAsText(trip: Trip, events: List<EventItem>, context: Context) {
    val text = getTripAsText(trip, events)
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, text)
    val chooserIntent = Intent.createChooser(intent, "Share with:")
    context.startActivity(chooserIntent)
}