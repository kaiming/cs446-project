package com.example.travelbook.trips.models

import com.google.firebase.firestore.DocumentId

data class Trip(
    @DocumentId
    val tripId: String,
    val tripName: String,
    val startDate: String,
    val endDate: String,
    val budget: String,
    var isArchived: Boolean,
    var participants: List<String> = emptyList()
) {
    constructor() : this("", "", "","", "", false)
}

fun createTrip(
    tripName: String,
    startDate: String,
    endDate: String,
    budget: String,
    isArchived: Boolean,
    participants: List<String> = emptyList()
): Trip {
    return Trip(
        tripId = "",
        tripName = tripName,
        startDate = startDate,
        endDate = endDate,
        budget = budget,
        isArchived = isArchived,
        participants = participants
    )
}
