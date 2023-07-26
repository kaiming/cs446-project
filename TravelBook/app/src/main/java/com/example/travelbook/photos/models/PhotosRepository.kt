package com.example.travelbook.photos.models

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PhotosRepository {

    private val database = Firebase.firestore

    fun fetchPhotosForUser(onComplete: (List<Photo>) -> Unit, onError: (Exception) -> Unit) {
        val photosCollection = database.collection("photos")
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            onComplete(emptyList()) // Return an empty list of photos
            return
        }

        val userId = currentUser.uid

        photosCollection
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val photosList = mutableListOf<Photo>()
                for (document in querySnapshot.documents) {
                    val imageUrl = document.getString("imageUrl") ?: ""
                    val date = document.getString("date") ?: ""
                    val userId = document.getString("userId") ?: ""
                    val tripId = document.getString("tripId") ?: ""
                    val photo = Photo(imageUrl = imageUrl, date = date, userId = userId, tripId = tripId)
                    photosList.add(photo)
                }
                onComplete(photosList)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }
}