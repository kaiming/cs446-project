package com.example.travelbook.trips.models

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class EventRepository {

    private val database = Firebase.firestore

    fun addEvent(tripId: String, event: EventItem) {
        database.collection("trips")
            .document(tripId)
            .collection("events")
            .add(event)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Event added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding event", e)
            }
    }

    fun deleteEvent(tripId: String, eventId: String) {
        database.collection("trips")
            .document(tripId)
            .collection("events")
            .document(eventId)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully deleted!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting document", e)
            }
    }

    fun editEvent(userId: String, tripId: String, eventId: String, event: EventItem) {
        database.collection("trips")
            .document(tripId)
            .collection("events")
            .document(eventId)
            .set(event, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating document", e)
            }
    }

    suspend fun getAllEventsByTripId(userId: String, tripId: String): List<EventResponse> {
        val eventList = mutableListOf<EventResponse>()

        val snapshot = database.collection("trips")
            .document(tripId)
            .collection("events")
            .get()
            .await()

        for (document in snapshot.documents) {
            document.toObject(EventItem::class.java)?.let { event ->
                eventList.add(EventResponse(event))
            }
        }
        return eventList
    }
}