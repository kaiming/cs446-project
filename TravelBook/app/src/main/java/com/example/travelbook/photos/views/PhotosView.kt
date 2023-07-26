package com.example.travelbook.photos.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.travelbook.photos.models.Photo
import com.example.travelbook.photos.viewModels.PhotosViewModel
import com.example.travelbook.ui.theme.Padding
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PhotosView(
    viewModel: PhotosViewModel,
    tripId: String?,
    onNavigateToPhotos: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    // State to hold the list of photos
    val photosState = remember { mutableStateOf<List<Photo>>(emptyList()) }

    // Fetch photos for the user when the composable is first composed
    LaunchedEffect(Unit) {
        viewModel.fetchPhotosForUser(
            tripId,
            onComplete = { photos ->
                photosState.value = photos
            },
            onError = {
                // Handle the error here, e.g., display an error message
            }
        )
    }

    Box(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            androidx.compose.material3.Text(
                text = "Trip Photos",
                fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                fontSize = 32.sp,
                modifier = Modifier.padding(Padding.PaddingSmall.size)
            )

            // Display the list of photos
            PhotoList(photosState.value)
        }
    }
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

