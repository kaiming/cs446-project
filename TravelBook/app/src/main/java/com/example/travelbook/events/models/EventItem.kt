package com.example.travelbook.events.models

import com.google.firebase.firestore.DocumentId


data class EventItem(
    @DocumentId
    var eventId: String,
    var name: String,
    var date: String,
    var startTime: String,
    var endTime: String,
    var location: String,
    var cost: String
) {
    constructor() : this(
        eventId = "",
        name = "",
        date = "",
        startTime = "",
        endTime = "",
        location = "",
        cost = ""
    )
}

//data class EventResponse(
//    var events: EventItem
//)
