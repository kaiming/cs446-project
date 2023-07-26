// Random utility functions that can be used anywhere in the app

import android.content.ContentValues.TAG
import android.util.Log
import com.example.travelbook.events.models.EventItem
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

    // Retrieve a user's id from their email
    suspend fun getUserIdByEmail(email: String): String? {

        // i9lRjx0PyJatIo3oJP21
        Log.d(TAG, "UtilityFunctions:getUserIdByEmail: $email")
        try {
            val querySnapshot = firestore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                return querySnapshot.documents[0].get("id") as? String
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

    // Make text from a trip, including all events (NO PARTICIPANTS)
    fun getTripAsText(trip: Trip, events: List<EventItem>): String {
        var tripText = "Trip: ${trip.tripName}\n"
        tripText += "Start Date: ${trip.startDate}\n"
        tripText += "End Date: ${trip.endDate}\n"
        tripText += "Budget: ${trip.budget}\n\n"
        tripText += "Events:\n"
        events.forEach { event ->
            tripText += "\t${event.name}\n"
            tripText += "\t\tLocation: ${event.location}\n"
            tripText += "\t\tDate: ${event.startDate} at ${event.startTime}\n"
            tripText += "\t\tDate: ${event.endDate} at ${event.endTime}\n"
            tripText += "\t\tCost: ${event.cost}\n\n"
        }
        return tripText
    }
}
