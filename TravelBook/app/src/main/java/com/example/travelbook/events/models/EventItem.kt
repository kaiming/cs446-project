package com.example.travelbook.events.models

import com.google.firebase.firestore.DocumentId


data class EventItem(
    @DocumentId
    var eventId: String,
    var tripId: String,
    var name: String,
    var location: String,
    var startDate: String,
    var endDate: String,
    var startTime: String,
    var endTime: String,
    var cost: String
) {
    constructor() : this(
        eventId = "",
        tripId = "",
        name = "",
        location = "",
        startDate = "",
        endDate = "",
        startTime = "",
        endTime = "",
        cost = ""
    )
}

//data class EventResponse(
//    var events: EventItem
//)
