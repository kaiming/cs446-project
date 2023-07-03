package com.example.travelbook.events.views

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.events.viewModels.AddEventViewModel
import com.example.travelbook.ui.theme.Padding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventView(
    viewModel: AddEventViewModel,
    onNavigateToEvents: (String) -> Unit,
    tripId: String?,
    modifier: Modifier = Modifier
) {
    if (tripId !is String) return

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var eventName by remember { mutableStateOf(TextFieldValue("")) }
    var eventLocation by remember { mutableStateOf(TextFieldValue("")) }
    var eventStartDate by remember { mutableStateOf(
        LocalDate.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    ) }
    var eventEndDate by remember { mutableStateOf(
        LocalDate.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    ) }
    var eventStartTime by remember { mutableStateOf(
        LocalTime.of(
            calendar.get(Calendar.HOUR),
            calendar.get(Calendar.MINUTE)
        )
    ) }
    var eventEndTime by remember { mutableStateOf(
        LocalTime.of(
            calendar.get(Calendar.HOUR),
            calendar.get(Calendar.MINUTE)
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
    startDatePicker.datePicker.minDate = calendar.timeInMillis

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
            TextField(
                value = eventLocation,
                onValueChange = {
                    eventLocation = it
                },
                label = { Text(text = "Event Location") },
                placeholder = { Text(text = "Location of the Event") },
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                modifier = Modifier.padding(Padding.PaddingSmall.size)
            )
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
                modifier = Modifier.padding(Padding.PaddingSmall.size)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    viewModel.addEventItem(
                        EventItem(
                            eventId =  UUID.randomUUID().toString(),
                            name = eventName.text,
                            location = eventLocation.text,
                            startDate = eventStartDate.toString(),
                            endDate = eventEndDate.toString(),
                            startTime = eventStartTime.toString(),
                            endTime = eventEndTime.toString(),
                            cost = eventCost.text
                        ),
                        tripId = tripId
                    )
                    onNavigateToEvents(tripId)
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