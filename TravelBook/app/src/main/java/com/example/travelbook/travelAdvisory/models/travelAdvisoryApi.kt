package com.example.travelbook.travelAdvisory.models

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TravelAdvisoryAPIService {
    @GET("api")
    suspend fun getAdvisory(@Query("countrycode") countryCode: String): Response<TravelAdvisoryResponse>

    companion object {
        private const val BASE_URL = "https://www.travel-advisory.info/"

        private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }

        val service: TravelAdvisoryAPIService by lazy {
            retrofit.create(TravelAdvisoryAPIService::class.java)
        }
    }
}

interface TravelAdvisoryAPIClientHelper {
    fun getTravelAdvisories(
        countryCodes: List<String>
    ): Flow<List<CountryAdvisory>>
}