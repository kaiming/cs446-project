package com.example.travelbook.map.views

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.travelbook.events.models.EventItem
import com.example.travelbook.map.viewModels.MapViewModel
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PatternItem
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import java.time.LocalDate
import java.time.LocalTime
import kotlin.random.Random


@Composable
fun MapView(
    viewModel: MapViewModel,
    modifier: Modifier = Modifier
) {
    val uiSettings = remember { mutableStateOf(
        MapUiSettings(
            compassEnabled = true,
            zoomControlsEnabled = false
        )
    ) }
    LaunchedEffect(Unit) {
        viewModel.loadTrips()
    }
    val trips = viewModel.tripsFlow.collectAsStateWithLifecycle(initialValue = emptyList())

    Box(
        modifier = modifier
    ) {
        GoogleMap(
            uiSettings = uiSettings.value
        ) {
            trips.value.filter { !it.isArchived }.forEach { trip ->
                val events = viewModel.getEventsFlowByTripId(trip.tripId).collectAsStateWithLifecycle(initialValue = emptyList())
                val tripColor =  Color(
                    Random(seed = trip.tripId.hashCode()).nextInt(255),
                    Random(seed = trip.tripId.hashCode() + 1).nextInt(255),
                    Random(seed = trip.tripId.hashCode() + 2).nextInt(255),
                    200
                )

                val pattern = mutableListOf<PatternItem>()
                if(LocalDate.parse(trip.endDate) > LocalDate.now()) {
                    pattern.add(Dot())
                }

                val points = mutableListOf<LatLng>()
                val eventComparator = Comparator { event1: EventItem, event2: EventItem ->
                    val event1StartDate = LocalDate.parse(event1.startDate)
                    val event2StartDate = LocalDate.parse(event2.startDate)
                    val event1StartTime = LocalTime.parse(event1.startTime)
                    val event2StartTime = LocalTime.parse(event2.startTime)
                    when {
                        event1StartDate > event2StartDate -> 1
                        event1StartDate < event2StartDate -> -1
                        event1StartDate == event2StartDate && event1StartTime > event2StartTime -> 1
                        event1StartDate == event2StartDate && event1StartTime < event2StartTime -> -1
                        else -> 0
                    }
                }
                events.value.sortedWith(eventComparator).forEach { event ->
                    val latLngString = event.locationCoordinates.split(",")
                    val latitude: Double = latLngString[0].toDouble()
                    val longitude: Double = latLngString[1].toDouble()

                    val latLng = LatLng(latitude, longitude)

                    points.add(latLng)
                }
                Polyline(
                    points = points,
                    width = 20f,
                    color = tripColor,
                    jointType = JointType.ROUND,
                    endCap = RoundCap(),
                    startCap = RoundCap(),
                    geodesic = true,
                    pattern = pattern.ifEmpty { null }
                )
            }
        }
    }
}