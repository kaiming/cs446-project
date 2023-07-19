package com.example.travelbook.googlePrediction.models

import com.example.travelbook.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.Response
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePredictionAPIService {
    @GET("maps/api/place/autocomplete/json")
    suspend fun getPredictions(
        @Query("key") key: String = BuildConfig.MAPS_API_KEY,
        @Query("input") input: String
    ): Response<GooglePredictionResponse>

    companion object {
        private const val BASE_URL = "https://maps.googleapis.com/"

        private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }

        val googlePredictionAPIService: GooglePredictionAPIService by lazy {
            retrofit.create(GooglePredictionAPIService::class.java)
        }
    }
}

interface GooglePredictionAPIClientHelper {
    fun getPredictions(
        input: String
    ): Flow<GooglePredictionResponse>
}

