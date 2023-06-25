package com.example.travelbook.events.views

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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelbook.ui.theme.Padding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun AddTripView(
    modifier: Modifier = Modifier
) {
    var tripName by remember { mutableStateOf(TextFieldValue("")) }
    var tripLocation by remember { mutableStateOf(TextFieldValue("")) }
    var tripStartDate by remember { mutableStateOf(TextFieldValue("")) }
    var tripEndDate by remember { mutableStateOf(TextFieldValue("")) }

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
                modifier = Modifier.padding(Padding.PaddingSmall.size)
            )
            TextField(
                value = tripLocation,
                onValueChange = {
                    tripLocation = it
                },
                label = { Text(text = "Event Location") },
                placeholder = { Text(text = "Location of the Event") },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(Padding.PaddingSmall.size)
            )
            TextField(
                value = tripStartDate,
                onValueChange = {
                    tripStartDate = it
                },
                label = { Text(text = "Trip Start Date") },
                placeholder = { Text(text = "Start Date of the Trip") },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(Padding.PaddingSmall.size)
            )
            TextField(
                value = tripEndDate,
                onValueChange = {
                    tripEndDate = it
                },
                label = { Text(text = "Trip End Date") },
                placeholder = { Text(text = "End Date of the Trip") },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(Padding.PaddingSmall.size)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { /*TODO add view model*/ },
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