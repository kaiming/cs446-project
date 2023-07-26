package com.example.travelbook.events.models

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Date

class EventRepository {

    private val database = Firebase.firestore
    private val storageReference = Firebase.storage.reference

    fun uploadImageToFirebase(uri: Uri, date: String, tripId: String): Task<UploadTask.TaskSnapshot> {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: throw IllegalStateException("No user logged in")
        val imageReference = storageReference.child("images/$tripId/${uri.lastPathSegment}")
        val metadata = StorageMetadata.Builder()
            .setContentType("image/jpg")
            .setCustomMetadata("date", date)
            .setCustomMetadata("userId", userId)
            .setCustomMetadata("tripId", tripId)
            .build()

        return imageReference.putFile(uri, metadata)
    }

    fun getAllEventsByTripIdFlow(tripId: String?): Flow<List<EventItem>> = flow {
        if (tripId == null) {
            throw Exception();
        }

        val querySnapshot = database.collection("trips")
                .document(tripId)
                .collection("events")
                .get()
                .await()
        val events = querySnapshot.documents.mapNotNull { documentSnapshot ->
            documentSnapshot.toObject<EventItem>()
        }
        emit(events)
    }

    fun getEventByIdFlow(tripId: String, eventId: String): Flow<EventItem?> = callbackFlow {
        val documentRef = database.collection("trips")
            .document(tripId)
            .collection("events")
            .document(eventId)

        val eventDocument = documentRef.addSnapshotListener { documentSnapshot, exception ->
            if (exception != null) {
                close(exception)
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val event = documentSnapshot.toObject<EventItem>()
                trySend(event).isSuccess
            } else {
                trySend(null).isSuccess
            }
        }

        awaitClose { eventDocument.remove() }
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

    fun editEvent(tripId: String, eventId: String, event: EventItem) {
        database.collection("trips")
            .document(tripId)
            .collection("events")
            .document(eventId)
            .set(event)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating document", e)
            }
    }

    suspend fun getAllEventsByTripId(tripId: String): List<EventItem> {
        val eventList = mutableListOf<EventItem>()

        val snapshot = database.collection("trips")
            .document(tripId)
            .collection("events")
            .get()
            .await()

        for (document in snapshot.documents) {
            document.toObject(EventItem::class.java)?.let { event ->
                eventList.add(event)
            }
        }
        return eventList
    }

    fun addUserToTrip(tripId: String, userId: String) {
        database.collection("trips")
            .document(tripId)
            .update("participants", FieldValue.arrayUnion(userId))
            .addOnSuccessListener {
                Log.d(TAG, "User added to trip")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding user to trip", e)
            }
    }
}