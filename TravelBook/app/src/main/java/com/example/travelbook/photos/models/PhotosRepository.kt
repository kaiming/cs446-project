package com.example.travelbook.photos.models

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PhotosRepository {

    private val database = Firebase.firestore

    fun fetchPhotosForTrip(
        tripId: String?,
        onComplete: (List<Photo>) -> Unit,
        onError: (Exception) -> Unit
    ) {

        if (tripId == null) {
            return
        }
        // Get a reference to the Firebase Storage
        val storageReference = Firebase.storage.reference

        // Create a reference to the images folder for the specific tripId
        val tripImagesReference = storageReference.child("images").child(tripId)

        // Fetch the list of image URLs from the trip's images folder
        tripImagesReference.listAll()
            .addOnSuccessListener { listResult ->
                val photosList = mutableListOf<Photo>()

                // Iterate through the list of items in the folder
                for (item in listResult.items) {
                    // Get the download URL for each image
                    item.downloadUrl
                        .addOnSuccessListener { downloadUrl ->
                            // Get custom metadata for the image (date and userId)
                            item.metadata
                                .addOnSuccessListener { metadata ->
                                    val date = metadata.getCustomMetadata("date") ?: ""
                                    val userId = metadata.getCustomMetadata("userId") ?: ""

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