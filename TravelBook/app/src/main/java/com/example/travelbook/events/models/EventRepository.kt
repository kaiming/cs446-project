package com.example.travelbook.events.models

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class EventRepository {

    private val database = Firebase.firestore

    fun getAllEventsByTripIdFlow(tripId: String): Flow<List<EventResponse>> = flow {
        val querySnapshot = database.collection("trips")
                .document(tripId)
                .collection("events")
                .get()
                .await()
        val events = querySnapshot.documents.mapNotNull { documentSnapshot ->
            documentSnapshot.toObject<EventResponse>()
        }
        emit(events)
    }

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

    suspend fun getAllEventsByTripId(tripId: String): List<EventResponse> {
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