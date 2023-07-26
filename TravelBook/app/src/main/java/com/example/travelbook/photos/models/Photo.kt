package com.example.travelbook.photos.models

data class Photo(
    val imageUrl: String,
    val date: String,
    val userId: String,
    val tripId: String
)