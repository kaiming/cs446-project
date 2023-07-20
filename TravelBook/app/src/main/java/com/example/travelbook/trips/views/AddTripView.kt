package com.example.travelbook.events.views

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.trips.models.Trip
import com.example.travelbook.trips.viewModels.AddTripViewModel
import com.example.travelbook.ui.theme.Padding
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTripView(
    viewModel: AddTripViewModel,
    onNavigateToTrip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var tripName by remember { mutableStateOf(TextFieldValue("")) }
    var tripStartDate by remember { mutableStateOf(
        LocalDate.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    ) }
    var tripEndDate by remember { mutableStateOf(
        LocalDate.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
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

    var tripBudget by remember { mutableStateOf(TextFieldValue("")) }

    Box(
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Add Trip",
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Starts")
                TextButton(onClick = { startDatePicker.show() }) {
                    Text(tripStartDate.toString())
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ends")
                TextButton(onClick = { endDatePicker.show() }) {
                    Text(tripEndDate.toString())
                }
            }
            TextField(
                value = tripBudget,
                onValueChange = {
                    tripBudget = it
                },
                label = { Text(text = "Trip Budget") },
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                modifier = Modifier.padding(Padding.PaddingSmall.size)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    if(tripName.text.isNotBlank() && tripName.text.isNotBlank()) {
                        viewModel.addTripItem(
                            Trip(
                                UUID.randomUUID().toString(),
                                tripName.text,
                                tripStartDate.toString(),
                                tripEndDate.toString(),
                                tripBudget.toString(),
                                listOf("user1","user2"))
                        )
                        onNavigateToTrip()
                    }
                    else {
                        // TODO: Show error
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