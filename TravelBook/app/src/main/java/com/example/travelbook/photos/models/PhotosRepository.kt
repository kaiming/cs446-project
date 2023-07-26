package com.example.travelbook.photos.models

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PhotosRepository {

    private val database = Firebase.firestore

    fun fetchPhotosForUser(
        onComplete: (List<Photo>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        // Get a reference to the Firebase Storage
        val storageReference = Firebase.storage.reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            onComplete(emptyList())
            return
        }
        val userId = currentUser.uid

        // Create a reference to the user's images folder in Firebase Storage
        val userImagesReference = storageReference.child("images").child(userId)

        // Fetch the list of image URLs from the user's images folder
        userImagesReference.listAll()
            .addOnSuccessListener { listResult ->
                val photosList = mutableListOf<Photo>()

                // Iterate through the list of items in the folder
                for (item in listResult.items) {
                    // Get the download URL for each image
                    item.downloadUrl
                        .addOnSuccessListener { downloadUrl ->
                            // Get custom metadata for the image (date and tripId)
                            item.metadata
                                .addOnSuccessListener { metadata ->
                                    val date = metadata.getCustomMetadata("date") ?: ""
                                    val tripId = metadata.getCustomMetadata("tripId") ?: ""

                                    // Create a Photo object and add it to the list
                                    val photo = Photo(downloadUrl.toString(), date, userId, tripId)
                                    photosList.add(photo)

                                    // Check if all images have been processed
                                    if (photosList.size == listResult.items.size) {
                                        // Call the onComplete callback with the list of photos
                                        Log.d("PHOTOS", "${photosList.size}")
                                        onComplete(photosList)
                                    }
                                }
                        }
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

}