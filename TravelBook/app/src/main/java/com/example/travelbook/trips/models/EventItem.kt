package com.example.travelbook.trips.models


data class EventItem(
    @DocumentId
    var eventId: String,
    var name: String,
    var Date: String,
    var startTime: String,
    var location: String
) {
    constructor() : this("", "", "", "", "")
}

data class EventResponse(
    var event: EventItem
)
