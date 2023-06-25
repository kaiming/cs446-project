package com.example.travelbook.navigation.models

import java.util.Date


data class EventItem(
    var name: String,
    var Date: Date?,
    var startTime: Date?,
    var location: String
) {
    constructor() : this("", null, null, "")
}

data class EventResponse(
    var event: EventItem
)
