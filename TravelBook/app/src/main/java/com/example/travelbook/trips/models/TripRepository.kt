package com.example.travelbook.trips.models

import UtilityFunctions.getTripsWithParticipantEmails
import android.content.ContentValues
import android.util.Log
import com.example.travelbook.events.models.EventItem
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

private const val TAG = "TripRepository"

class TripRepository {

    // Singleton
    companion object {
        @Volatile
        private var INSTANCE: TripRepository? = null

        fun getInstance(): TripRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = TripRepository()
                INSTANCE = instance
                instance
            }
        }
    }

    private val database = Firebase.firestore

    fun archiveTrip(tripId: String) {
        database.collection("trips")
            .document(tripId)
            .update("archived", true) // don't know why its saving as "archived" even though it's called "isArchived" in the model
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Trip successfully archived")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error achiving trip", e)
            }
    }

    fun getAllTripsFlow(): Flow<List<Trip>> = flow {
        val querySnapshot = database.collection("trips")
            .get()
            .await()
        val trips = querySnapshot.documents.mapNotNull { documentSnapshot ->
            documentSnapshot.toObject<Trip>()
        }
        val tripsWithEmails = getTripsWithParticipantEmails(trips)
        emit(tripsWithEmails)
    }

    fun getAllTripsByUserIDFlow(userId: String? = null): Flow<List<Trip>> = flow {
        Log.d(TAG, "getAllTripsByUserIDFlow: $userId")
        if (userId != null) {
            Log.d(TAG, "getAllTripsByUserIDFlow: userId is not null")
            val querySnapshot = database
                .collection("trips")
                .whereArrayContains("participants", userId)
                .get()
                .await()
            val trips = querySnapshot.documents.mapNotNull { documentSnapshot ->
                documentSnapshot.toObject<Trip>()
            }
            val tripsWithEmails = getTripsWithParticipantEmails(trips)
            emit(tripsWithEmails)
        } else {
            Log.d(TAG, "getAllTripsByUserIDFlow: userId is null")
            emit(emptyList())
        }

    }

    fun getAllTripsByUserIdAndFilterForArchivedFlow(userId: String? = null): Flow<List<Trip>> = flow {
        Log.d(TAG, "getAllTripsByUserIdAndFilterForArchivedFlow: $userId")
        if (userId != null) {
            Log.d(TAG, "getAllTripsByUserIdAndFilterForArchivedFlow: userId is not null")
            val querySnapshot = database
                .collection("trips")
                .whereArrayContains("participants", userId)
                .whereEqualTo("archived", true)
                .get()
                .await()
            val trips = querySnapshot.documents.mapNotNull { documentSnapshot ->
                documentSnapshot.toObject<Trip>()
            }
            val tripsWithEmails = getTripsWithParticipantEmails(trips)
            emit(tripsWithEmails)
        } else {
            Log.d(TAG, "getAllTripsByUserIdAndFilterForArchivedFlow: userId is null")
            emit(emptyList())
        }
    }

    fun getTripByIdFlow(tripId: String): Flow<Trip?> = callbackFlow {
        Log.d(TAG, "getTripByIdFlow: $tripId")
        val documentRef = database.collection("trips")
            .document(tripId)

        val tripDocument = documentRef.addSnapshotListener { documentSnapshot, exception ->
            if (exception != null) {
                close(exception)
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val trip = documentSnapshot.toObject<Trip>()
                trySend(trip).isSuccess
            } else {
                trySend(null).isSuccess
            }
        }

        awaitClose { tripDocument.remove() }
    }

    // Get trips based on user id, participants contains a list of user ids
    fun getAllTripsByUserID(userId: String): List<Trip> {
        Log.d(TAG, "getAllTripsByUserID: $userId")
        val trips = mutableListOf<Trip>()
        database.collection("trips")
            .whereArrayContains("participants", userId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val trip = document.toObject(Trip::class.java)
                    trips.add(trip)
                }
            }
        return trips
    }

    fun getTripByTripIDFlow(tripId: String?): Flow<Trip?> = flow {
        if (tripId == null) {
            throw Exception();
        }

        val documentSnapshot = database.collection("trips")
            .document(tripId)
            .get()
            .await()
        var trip = documentSnapshot.toObject<Trip>()
        emit(trip)
    }

    // Get trip based on trip id
    fun getTripByTripID(tripId: String): Trip {
        Log.d(TAG, "getTripByTripID: $tripId")
        var trip = Trip()
        database.collection("trips")
            .document(tripId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    trip = document.toObject(Trip::class.java)!!
                }
            }
        return trip
    }

    // Add trip
    fun addTrip(trip: Trip) {
        database.collection("trips")
            .add(trip)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Trip added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding trip", e)
            }
    }

    fun deleteTrip(tripId: String) {
        database.collection("trips")
            .document(tripId)
            .delete()
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot successfully deleted!")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error deleting document", e)
            }
    }

    fun editTrip(tripId: String, trip: Trip) {
        database.collection("trips")
            .document(tripId)
            .set(trip)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error updating document", e)
            }
    }

    // Add user to trip
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
