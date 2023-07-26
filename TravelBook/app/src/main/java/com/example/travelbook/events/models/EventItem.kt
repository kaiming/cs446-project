package com.example.travelbook.events.models

import com.google.firebase.firestore.DocumentId


data class EventItem(
    @DocumentId
    var eventId: String,
    var name: String,
    var location: String,
    var locationCoordinates: String,
    var locationCountry: String,
    var startDate: String,
    var endDate: String,
    var startTime: String,
    var endTime: String,
    var cost: String
) {
    constructor() : this(
        eventId = "",
        name = "",
        location = "",
        locationCoordinates = "",
        locationCountry = "",
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
