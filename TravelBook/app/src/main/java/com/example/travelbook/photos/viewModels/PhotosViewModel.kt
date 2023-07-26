package com.example.travelbook.photos.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelbook.UserDataSource
import com.example.travelbook.photos.models.Photo
import com.example.travelbook.photos.models.PhotosRepository
import com.google.firebase.firestore.FirebaseFirestore

class PhotosViewModel(private val repository: PhotosRepository
                      , private val userDataSource: UserDataSource): ViewModel() {

    fun fetchPhotosForUser(
        onComplete: (List<Photo>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        repository.fetchPhotosForUser(onComplete, onError)
    }
}

class PhotosViewModelFactory(private val repository: PhotosRepository, private val userDataSource: UserDataSource)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhotosViewModel::class.java)) {
            return PhotosViewModel(repository, userDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}