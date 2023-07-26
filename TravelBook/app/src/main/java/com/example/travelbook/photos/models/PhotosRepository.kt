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

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            onComplete(emptyList())
            return
        }
        val userId = currentUser.uid
        var count = 0


        // Create a reference to the images folder for the specific tripId
        val tripImagesReference = storageReference.child("images").child(userId)

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
                                    val _tripId = metadata.getCustomMetadata("tripId") ?: ""

                                    if (_tripId == tripId) {
                                        // Create a Photo object and add it to the list
                                        val photo = Photo(downloadUrl.toString(), date, userId, tripId)
                                        photosList.add(photo)
                                    }
                                    count += 1



                                    Log.d("PHOTOS_", "${photosList.size}")
                                    // Check if all images have been processed
                                    if (count == listResult.items.size) {
                                        // Call the onComplete callback with the list of photos
                                        Log.d("PHOTOS", "${photosList.size}")
                                        onComplete(photosList)
                                    } else {
                                        Log.d("PHOTO_SIZE", "${photosList.size}")
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