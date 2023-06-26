package com.example.travelbook.events.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.events.viewModels.AddEventViewModel
import com.example.travelbook.ui.theme.Padding
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventView(
    viewModel: AddEventViewModel,
    modifier: Modifier = Modifier
) {
    var eventName by remember { mutableStateOf(TextFieldValue("")) }
    var eventLocation by remember { mutableStateOf(TextFieldValue("")) }
    var eventDate by remember { mutableStateOf(TextFieldValue("")) }
    var eventStartTime by remember { mutableStateOf(TextFieldValue("")) }
    var eventEndTime by remember { mutableStateOf(TextFieldValue("")) }
    var eventCost by remember { mutableStateOf(TextFieldValue("")) }

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
                modifier = Modifier.padding(Padding.PaddingSmall.size)
            )
            TextField(
                value = eventDate,
                onValueChange = {
                    eventDate = it
                },
                label = { Text(text = "Event Date") },
                placeholder = { Text(text = "Date of the Event") },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(Padding.PaddingSmall.size)
            )
            TextField(
                value = eventStartTime,
                onValueChange = {
                    eventStartTime = it
                },
                label = { Text(text = "Event Start Time") },
                placeholder = { Text(text = "Start Time of the Event") },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(Padding.PaddingSmall.size)
            )
            TextField(
                value = eventEndTime,
                onValueChange = {
                    eventEndTime = it
                },
                label = { Text(text = "Event End Time") },
                placeholder = { Text(text = "End Time of the Event") },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(Padding.PaddingSmall.size)
            )
            TextField(
                value = eventCost,
                onValueChange = {
                    eventCost = it
                },
                label = { Text(text = "Event Cost") },
                placeholder = { Text(text = "Cost of the Event") },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(Padding.PaddingSmall.size)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { viewModel.addEventItem(
                    EventItem(
                        UUID.randomUUID().toString(),
                        eventName.text,
                        eventDate.text,
                        eventStartTime.text,
                        eventEndTime.text,
                        eventLocation.text,
                        eventCost.text
                    )
                ) },
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