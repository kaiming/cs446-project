// Random utility functions that can be used anywhere in the app

import android.content.ContentValues.TAG
import android.util.Log
import com.example.travelbook.trips.models.Trip
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

object UtilityFunctions {

    private val firestore = FirebaseFirestore.getInstance()

    // Retrieve a user's email from their id
    fun getEmailByUserId(userId: String, callback: (String?) -> Unit) {
        Log.d(TAG, "UtilityFunctions: $userId")
        firestore.collection("users")
            .whereEqualTo("id", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.size > 0) {
                    val email = querySnapshot.documents[0].get("email") as String
                    callback(email)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                callback(null)
            }
    }

    // Ammend trip object to contain emails of participants instead of user ids
    fun getTripWithParticipantEmails(trip: Trip): Trip {
        val tripWithParticipantEmails = trip
        val participantEmails = mutableListOf<String>()
        trip.participants.forEach { participant ->
            getEmailByUserId(participant) { email ->
                if (email != null) {
                    participantEmails.add(email)
                    if (participantEmails.size == trip.participants.size) {
                        tripWithParticipantEmails.participants = participantEmails
                    }
                }
            }
        }
        return tripWithParticipantEmails
    }

    // Replace list of trip objects to contain emails
    fun getTripsWithParticipantEmails(trips: List<Trip>): List<Trip> {
        val tripsWithParticipantEmails = mutableListOf<Trip>()
        trips.forEach { trip ->
            tripsWithParticipantEmails.add(getTripWithParticipantEmails(trip))
        }
        return tripsWithParticipantEmails
    }
}