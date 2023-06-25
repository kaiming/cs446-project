package com.example.travelbook.trips.models

import com.google.firebase.firestore.DocumentId

data class Trip(
    @DocumentId
    val tripId: String,
    val tripName: String,
    val startDate: String,
    val endDate: String,
    val participants: List<String> = emptyList(),
    val events: List<EventItem> = emptyList()
) {
    constructor() : this("", "", "", "")
}

fun createTrip(
    tripName: String,
    startDate: String,
    endDate: String,
    participants: List<String> = emptyList(),
    events: List<EventItem> = emptyList()
): Trip {
    return Trip(
        tripId = "",
        tripName = tripName,
        startDate = startDate,
        endDate = endDate,
        participants = participants,
        events = events
    )
}
