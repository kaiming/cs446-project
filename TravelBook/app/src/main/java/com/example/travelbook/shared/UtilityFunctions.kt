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
                callback(querySnapshot.documents[0].id)
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
            getEmailByUserId(participant) { userId ->
                if (userId != null) {
                    firestore.collection("users")
                        .document(userId)
                        .get()
                        .addOnSuccessListener { documentSnapshot ->
                            val email = documentSnapshot.get("email") as String
                            participantEmails.add(email)
                            if (participantEmails.size == trip.participants.size) {
                                tripWithParticipantEmails.participants = participantEmails
                            }
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