package com.example.travelbook.trips.views

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.travelbook.trips.models.Trip
import com.example.travelbook.trips.viewModels.ModifyTripViewModel
import com.example.travelbook.googlePrediction.models.GooglePredictionResponse
import com.example.travelbook.googlePrediction.models.emptyGooglePredictionResponse
import com.example.travelbook.ui.theme.Padding
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyTripView(
    viewModel: ModifyTripViewModel,
    tripId: String?,
    onNavigateToEvents: () -> Unit,
    onNavigateToTrips: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (tripId == null) return

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val trip = viewModel.getTripById(tripId).collectAsState(null).value ?: return

    var tripName by remember { mutableStateOf(TextFieldValue(trip.tripName)) }
    var tripStartDate by remember { mutableStateOf(
        LocalDate.parse(trip.startDate, DateTimeFormatter.ISO_LOCAL_DATE)
    ) }
    var tripEndDate by remember { mutableStateOf(
        LocalDate.parse(trip.endDate, DateTimeFormatter.ISO_LOCAL_DATE)
    ) }

    val startDatePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            tripStartDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth)
        },
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    )
    startDatePicker.datePicker.minDate = calendar.timeInMillis

    val endDatePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            tripEndDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth)
        },
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    )
    endDatePicker.datePicker.minDate = tripStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    Box(
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Modify Trip",
                fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                fontSize = 32.sp,
                modifier = Modifier.padding(Padding.PaddingSmall.size)
            )
            TextField(
                value = tripName,
                onValueChange = {
                    tripName = it
                },
                label = { Text(text = "Trip Name") },
                placeholder = { Text(text = "Name of the Trip") },
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                modifier = Modifier.padding(Padding.PaddingSmall.size)
            )
            Text("Starts")
            TextButton(onClick = { startDatePicker.show() }) {
                Text(tripStartDate.toString())
            }
            Text("Ends")
            TextButton(onClick = { endDatePicker.show() }) {
                Text(tripEndDate.toString())
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    viewModel.deleteTripItem(
                        tripId
                    )
                    onNavigateToTrips()
                },
                modifier = Modifier.padding(Padding.PaddingMedium.size)
            ) {
                Text(
                    text = "Delete",
                    fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(Padding.PaddingSmall.size)
                )
            }
            Button(
                onClick = {
                    if(tripName.text.isNotBlank()) {
                        viewModel.modifyTripItem(
                            tripId,
                            Trip(
                                tripId =  trip.tripId,
                                tripName = tripName.text,
                                startDate = tripStartDate.toString(),
                                endDate = tripEndDate.toString(),
                                participants = trip.participants
                            )
                        )
                        onNavigateToEvents()
                    } else {
                        // TODO: ERROR modal
                    }
                },
                modifier = Modifier.padding(Padding.PaddingMedium.size)
            ) {
                Text(
                    text = "Save",
                    fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(Padding.PaddingSmall.size)
                )
            }
        }
    }
}

