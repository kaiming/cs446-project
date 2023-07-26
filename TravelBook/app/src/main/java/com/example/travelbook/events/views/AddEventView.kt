package com.example.travelbook.events.views

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.events.viewModels.AddEventViewModel
import com.example.travelbook.googlePrediction.models.GooglePredictionResponse
import com.example.travelbook.googlePrediction.models.emptyGooglePredictionResponse
import com.example.travelbook.ui.theme.Padding
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventView(
    viewModel: AddEventViewModel,
    onNavigateToEvents: () -> Unit,
    tripId: String?,
    modifier: Modifier = Modifier
) {
    if (tripId !is String) return

    val trip = viewModel.getTripByTripId(tripId).collectAsState(null).value ?: return

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var eventName by remember { mutableStateOf(TextFieldValue("")) }
    var eventLocation by remember { mutableStateOf(TextFieldValue("")) }
    var eventLocationCoordinates by remember { mutableStateOf("") }
    var eventLocationCountry by remember { mutableStateOf("") }
    var eventStartDate by remember { mutableStateOf(
        LocalDate.of(
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH] + 1,
            calendar[Calendar.DAY_OF_MONTH]
        )
    ) }
    var eventEndDate by remember { mutableStateOf(
        LocalDate.of(
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH] + 1,
            calendar[Calendar.DAY_OF_MONTH]
        )
    ) }
    var eventStartTime by remember { mutableStateOf(
        LocalTime.of(
            calendar[Calendar.HOUR],
            calendar[Calendar.MINUTE]
        )
    ) }
    var eventEndTime by remember { mutableStateOf(
        LocalTime.of(
            calendar[Calendar.HOUR],
            calendar[Calendar.MINUTE]
        )
    ) }
    var eventCost by remember { mutableStateOf(TextFieldValue("")) }

    val startDatePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            eventStartDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth)
        },
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    )
    startDatePicker.datePicker.minDate = LocalDate.parse(trip.startDate).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    startDatePicker.datePicker.maxDate = LocalDate.parse(trip.endDate).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    val endDatePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            eventEndDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth)
        },
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    )
    endDatePicker.datePicker.minDate = eventStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    endDatePicker.datePicker.maxDate = LocalDate.parse(trip.endDate).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    val startTimePicker = TimePickerDialog(
        context,
        { _, selectedHour: Int, selectedMinute: Int ->
            eventStartTime = LocalTime.of(selectedHour, selectedMinute)
        },
        calendar[Calendar.HOUR],
        calendar[Calendar.MINUTE],
        false
    )

    val endTimePicker = TimePickerDialog(
        context,
        { _, selectedHour: Int, selectedMinute: Int ->
            eventEndTime = LocalTime.of(selectedHour, selectedMinute)
        },
        calendar[Calendar.HOUR],
        calendar[Calendar.MINUTE],
        false
    )

    val googlePredictionResponseResource: GooglePredictionResponse by viewModel.getPredictions(
        input = eventLocation.text
    ).collectAsStateWithLifecycle(initialValue = emptyGooglePredictionResponse)

    val placesClient = Places.createClient(LocalContext.current)

    Box(
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Add Event",
                fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                fontSize = 32.sp,
                modifier = Modifier.padding(Padding.PaddingSmall.size)
            )
            TextField(
                value = eventName,
                onValueChange = {
                    eventName = it
                },
                label = { Text(text = "Event Name") },
                placeholder = { Text(text = "Name of the Event") },
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                modifier = Modifier.padding(Padding.PaddingSmall.size)
            )

            var exp by remember { mutableStateOf(false) }
            val focusRequester = remember { FocusRequester() }
            ExposedDropdownMenuBox(expanded = exp, onExpandedChange = { exp = !exp }) {
                TextField(
                    value = eventLocation,
                    onValueChange = {
                        eventLocation = it
                    },
                    label = { Text(text = "Event Location") },
                    placeholder = { Text(text = "Location of the Event") },
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    modifier = Modifier
                        .padding(Padding.PaddingSmall.size)
                        .menuAnchor()
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            if (!it.isFocused) {
                                exp = false
                            }
                        }
                )
                if (googlePredictionResponseResource.predictions.isNotEmpty()) {
                    ExposedDropdownMenu(expanded = exp, onDismissRequest = { }) {
                        googlePredictionResponseResource.predictions.forEach { prediction ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = prediction.description,
                                        modifier = Modifier
                                            .padding(4.dp)
                                    )
                                },
                                onClick = {
                                    val placeFields = listOf(Place.Field.LAT_LNG, Place.Field.ADDRESS_COMPONENTS)
                                    val request = FetchPlaceRequest.newInstance(
                                        prediction.place_id,
                                        placeFields
                                    )

                                    placesClient.fetchPlace(request)
                                        .addOnSuccessListener { response: FetchPlaceResponse ->
                                            eventLocationCoordinates =
                                                "${response.place.latLng?.latitude},${response.place.latLng?.longitude}"

                                            val addressComponents = response.place.addressComponents?.asList()
                                            if (addressComponents != null) {
                                                val countryComponent = addressComponents.find { component ->
                                                    component.types.contains("country")
                                                }
                                                if (countryComponent != null) {
                                                    eventLocationCountry = countryComponent.shortName ?: "xx"
                                                } else {
                                                    eventLocationCountry = "yy"
                                                }
                                            } else {
                                                eventLocationCountry = "zz"
                                            }

                                            Log.d(
                                                "AddEventView",
                                                "Event location $eventLocationCoordinates"
                                            )
                                        }.addOnFailureListener { exception: Exception ->
                                            if (exception is ApiException) {
                                                Log.d("AddEventView", "Failed to get place latLng")
                                            }
                                        }
                                    eventLocation = TextFieldValue(prediction.description)
                                    exp = false
                                }
                            )
                        }
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Starts")
                TextButton(onClick = { startDatePicker.show() }) {
                    Text(eventStartDate.toString())
                }
                TextButton(onClick = { startTimePicker.show() }) {
                    Text(eventStartTime.toString())
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ends")
                TextButton(onClick = { endDatePicker.show() }) {
                    Text(eventEndDate.toString())
                }
                TextButton(onClick = { endTimePicker.show() }) {
                    Text(eventEndTime.toString())
                }
            }
            TextField(
                value = eventCost,
                onValueChange = {
                    eventCost = it
                },
                label = { Text(text = "Event Cost") },
                placeholder = { Text(text = "Cost of the Event") },
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(Padding.PaddingSmall.size)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    if(eventName.text.isNotBlank() && eventLocation.text.isNotBlank() && eventLocationCoordinates.isNotBlank() && eventCost.text.isNotBlank()) {
                        viewModel.addEventItem(
                            EventItem(
                                eventId =  UUID.randomUUID().toString(),
                                name = eventName.text,
                                location = eventLocation.text,
                                locationCoordinates = eventLocationCoordinates,
                                locationCountry = eventLocationCountry,
                                startDate = eventStartDate.toString(),
                                endDate = eventEndDate.toString(),
                                startTime = eventStartTime.toString(),
                                endTime = eventEndTime.toString(),
                                cost = eventCost.text
                            ),
                            tripId = tripId
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