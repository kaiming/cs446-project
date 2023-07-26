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
            onComplete(emptyList())
            return
        }

        val storageReference = Firebase.storage.reference
        val tripImagesReference = storageReference.child("images/$tripId")

        tripImagesReference.listAll()
            .addOnSuccessListener { listResult ->
                val photosList = mutableListOf<Photo>()
                if (listResult.items.isEmpty()) {
                    onComplete(photosList)
                    return@addOnSuccessListener
                }

                for (item in listResult.items) {
                    item.downloadUrl.addOnSuccessListener { downloadUrl ->
                        item.metadata.addOnSuccessListener { metadata ->
                            val date = metadata.getCustomMetadata("date") ?: ""
                            val userIdFromMeta = metadata.getCustomMetadata("userId") ?: ""

                            val photo = Photo(downloadUrl.toString(), date, userIdFromMeta, tripId)
                            photosList.add(photo)

                            // This condition ensures we call onComplete only once, after processing the last item.
                            if (item == listResult.items.last()) {
                                onComplete(photosList)
                            }
                        }.addOnFailureListener { exception ->
                            onError(exception)
                        }
                    }.addOnFailureListener { exception ->
                        onError(exception)
                    }
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }



}