package com.example.travelbook.photos.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.travelbook.photos.models.Photo
import com.example.travelbook.photos.viewModels.PhotosViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PhotosView(
    viewModel: PhotosViewModel,
    onNavigateToPhotos: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    // State to hold the list of photos
    val photosState = remember { mutableStateOf<List<Photo>>(emptyList()) }

    // Fetch photos for the user when the composable is first composed
    LaunchedEffect(Unit) {
        viewModel.fetchPhotosForUser(
            onComplete = { photos ->
                photosState.value = photos
            },
            onError = {
                // Handle the error here, e.g., display an error message
            }
        )
    }

    // Other UI elements...
    // testing for now...

    // Display the list of photos
    PhotoList(photosState.value)
}

@Composable
fun PhotoItem(
    photo: Photo,
) {
    // You can use any image loading library like Coil or Glide to load the image
    Image(
        painter = rememberAsyncImagePainter(photo.imageUrl),
        contentDescription = "Photo",
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Adjust the height as needed
    )

    // Display other information about the photo, e.g., date
    Text(
        text = "Date: ${photo.date}",
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun PhotoList(photos: List<Photo>) {
    LazyColumn {
        items(photos) { photo ->
            PhotoItem(photo)
        }
    }
}

