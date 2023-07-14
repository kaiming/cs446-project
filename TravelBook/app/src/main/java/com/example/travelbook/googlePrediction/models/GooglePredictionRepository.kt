package com.example.travelbook.googlePrediction.models

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GooglePredictionRepository: GooglePredictionAPIClientHelper {
    override fun getPredictions(
        input: String
    ): Flow<GooglePredictionResponse> = flow {
        val response = GooglePredictionAPIService.googlePredictionAPIService.getPredictions(input = input)

        if(response.isSuccessful) {
            response.body()?.let { emit(it) }
        } else {
            // TODO: ERROR CASE
        }
    }
}