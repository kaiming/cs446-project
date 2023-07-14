package com.example.travelbook.googlePrediction.models

data class GooglePredictionTerm(
    val offset: Int,
    val value: String
)

data class GooglePrediction(
    val description: String,
    val terms: List<GooglePredictionTerm>
)
data class GooglePredictionResponse(
    val predictions: List<GooglePrediction>
)

val emptyGooglePredictionResponse = GooglePredictionResponse(emptyList())
