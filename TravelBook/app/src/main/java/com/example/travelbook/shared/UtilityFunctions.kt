// Random utility functions that can be used anywhere in the app

import android.content.ContentValues.TAG
import android.util.Log
import com.example.travelbook.trips.models.Trip
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

object UtilityFunctions {

    private val firestore = FirebaseFirestore.getInstance()

    // Retrieve a user's email from their id
    suspend fun getEmailByUserId(userId: String): String? {
        Log.d(TAG, "UtilityFunctions: $userId")
        try {
            val querySnapshot = firestore.collection("users")
                .whereEqualTo("id", userId)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                return querySnapshot.documents[0].get("email") as? String
            }
        } catch (exception: Exception) {
            Log.w(TAG, "Error getting documents: ", exception)
        }
        return null
    }

    // Ammend trip object to contain emails of participants instead of user ids
    suspend fun getTripWithParticipantEmails(trip: Trip): Trip {
        val tripWithParticipantEmails = trip
        val participantEmails = mutableListOf<String>()
        trip.participants.forEach { participant ->
            val email = getEmailByUserId(participant)
            Log.d(TAG, "getTripWithParticipantEmails: $email")
            if (email != null) {
                participantEmails.add(email)
                if (participantEmails.size == trip.participants.size) {
                    tripWithParticipantEmails.participants = participantEmails
                }
            }
        }
        return tripWithParticipantEmails
    }

    // Replace list of trip objects to contain emails
    suspend fun getTripsWithParticipantEmails(trips: List<Trip>): List<Trip> {
        val tripsWithParticipantEmails = mutableListOf<Trip>()
        trips.forEach { trip ->
            tripsWithParticipantEmails.add(getTripWithParticipantEmails(trip))
        }
        return tripsWithParticipantEmails
    }
}