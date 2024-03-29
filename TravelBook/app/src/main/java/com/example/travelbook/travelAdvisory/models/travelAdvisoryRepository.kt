package com.example.travelbook.travelAdvisory.models


import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TravelAdvisoryRepository : TravelAdvisoryAPIClientHelper {
    override fun getTravelAdvisories(countryCodes: List<String>): Flow<List<CountryAdvisory>> = flow {
        val countryAdvisories = mutableListOf<CountryAdvisory>()

        countryCodes.forEach { countryCode ->
            try {
                val response = TravelAdvisoryAPIService.service.getAdvisory(countryCode)
                Log.d("Travel Advisory", "getTravelAdvisories request sent for $countryCode")

                if (response.isSuccessful) {
                    Log.d("Travel Advisory", "getTravelAdvisories request successful for $countryCode")
                    val body = response.body()
                    body?.data?.get(countryCode)?.let { countryAdvisory ->
                        countryAdvisories.add(countryAdvisory)
                    }
                } else {
                    Log.d("Travel Advisory", "getTravelAdvisories request error for $countryCode")
                    // TODO: ERROR CASE
                }
            } catch (e: Exception) {
                Log.d("Travel Advisory", "getTravelAdvisories request failed for $countryCode: ${e.message}")
                // TODO: ERROR CASE
            }
        }

        emit(countryAdvisories)
    }
}
