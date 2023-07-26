package com.example.travelbook.travelAdvisory.views

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
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.events.viewModels.EventViewModel
import com.example.travelbook.navigation.models.NavigationItem
import com.example.travelbook.travelAdvisory.models.Advisory
import com.example.travelbook.travelAdvisory.viewModels.TravelAdvisoryViewModel
import com.example.travelbook.trips.views.TripCard
import com.example.travelbook.ui.theme.Padding
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@Composable
fun TravelAdvisoryView(
    viewModel: TravelAdvisoryViewModel,
    tripId: String?,
    modifier: Modifier = Modifier
) {
    if (tripId !is String) return

    val events = viewModel.getEventsFlowByTripId(tripId)
        .collectAsStateWithLifecycle(initialValue = emptyList())

    Box(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Travel Advisories",
                fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                fontSize = 32.sp,
                modifier = Modifier.padding(Padding.PaddingSmall.size)
            )
            TravelAdvisories(viewModel, events.value)
        }
    }
}

@Composable
private fun TravelAdvisories(
    viewModel: TravelAdvisoryViewModel,
    events: List<EventItem>
) {
    val countriesWithAdvisories = events.map { it.locationCountry }.distinct()
    val travelAdvisories = viewModel.getTravelAdvisories(countriesWithAdvisories)
        .collectAsState(initial = emptyList()).value

    if (travelAdvisories.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.error)
        ) {
            items(travelAdvisories) {
                Text(
                    text = "${it.name}: ${it.advisory.message}",
                    color = Color.White,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }
        }
    }
}
