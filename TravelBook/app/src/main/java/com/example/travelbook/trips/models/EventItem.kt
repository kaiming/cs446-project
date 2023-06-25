package com.example.travelbook.trips.models

<<<<<<< HEAD
import com.google.firebase.firestore.DocumentId


data class EventItem(
    @DocumentId
    var eventId: String,
=======

data class EventItem(
>>>>>>> 7dc44f2... Trip and event models w/ repository
    var name: String,
    var Date: String,
    var startTime: String,
    var location: String
) {
<<<<<<< HEAD
    constructor() : this("", "", "", "", "")
=======
    constructor() : this("", "", "", "")
>>>>>>> 7dc44f2... Trip and event models w/ repository
}

data class EventResponse(
    var event: EventItem
)
