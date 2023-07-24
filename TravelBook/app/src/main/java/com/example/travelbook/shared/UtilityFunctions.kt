// Random utility functions that can be used anywhere in the app

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

object UtilityFunctions {

    private val firestore = FirebaseFirestore.getInstance()

    // Retrieve a user's id from their email
    fun getUserIdByEmail(email: String, callback: (String?) -> Unit) {
        firestore.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                callback(querySnapshot.documents[0].id)
            }
            .addOnFailureListener { exception ->
                callback(null)
            }
    }
}