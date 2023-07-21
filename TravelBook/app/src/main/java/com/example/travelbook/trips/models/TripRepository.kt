package com.example.travelbook.trips.models

import android.content.ContentValues
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

    fun temp(userId: String) { // TEST
        val querySnapshot = database
            .collection("trips")
            .whereArrayContains("participants", userId)
            .whereEqualTo("archived", true)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Log the entire QuerySnapshot
                Log.d("Firebase Query", "Total Documents: ${querySnapshot.size()}")
                for (document in querySnapshot) {
                    Log.d("Firebase Query", "Document ID: ${document.id}")
                    val data = document.data
                    Log.d("Firebase Query", "Data: $data")
                }
            }
    }

    fun getAllTripsByUserIdAndFilterForArchivedFlow(userId: String): Flow<List<Trip>> = flow {
        val querySnapshot = database
            .collection("trips")
            .whereArrayContains("participants", userId)
            .whereEqualTo("archived", true)
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
