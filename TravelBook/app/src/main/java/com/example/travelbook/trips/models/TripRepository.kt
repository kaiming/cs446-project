package com.example.travelbook.trips.models

import android.util.Log
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

    private val database = Firebase.firestore

    private fun Query.snapshotFlow(): Flow<QuerySnapshot> = callbackFlow {
        val listenerRegistration = addSnapshotListener { value, error ->
            if (error != null) {
                close()
                return@addSnapshotListener
            }
            if (value != null)
                trySend(value)
        }
        awaitClose {
            listenerRegistration.remove()
        }
    }

    fun getAllTripsByUserIDFlow(userId: String): Flow<List<Trip>> = flow {
        val querySnapshot = database
            .collection("trips")
            .whereArrayContains("participants", userId)
            .get()
            .await()
        val trips = querySnapshot.documents.mapNotNull { documentSnapshot ->
            documentSnapshot.toObject<Trip>()
        }
        emit(trips)
    }

    // Get trips based on user id, participants contains a list of user ids
    fun getAllTripsByUserID(userId: String): List<Trip> {
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

    // Get trip based on trip id
    fun getTripByTripID(tripId: String): Trip {
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
