package com.example.travelbook.googlePrediction.models

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GooglePredictionRepository: GooglePredictionAPIClientHelper {
    override fun getPredictions(
        input: String
    ): Flow<GooglePredictionResponse> = flow {
        val response = GooglePredictionAPIService.googlePredictionAPIService.getPredictions(input = input)
        Log.d("Google Predictions","getPredictions request sent")
        if(response.isSuccessful) {
            Log.d("Google Predictions","getPredictions request successful")
            response.body()?.let { emit(it) }
        } else {
            Log.d("Google Predictions","getPredictions request error")
            // TODO: ERROR CASE
        }
    }
}