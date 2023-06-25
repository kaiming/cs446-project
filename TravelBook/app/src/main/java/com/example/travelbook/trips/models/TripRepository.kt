package com.example.travelbook.trips.models

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "TripRepository"

class TripRepository {

    private val database = Firebase.firestore

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
        println("Hello!")
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
