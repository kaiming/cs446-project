package com.example.travelbook.travelAdvisory.models

data class Advisory(
    val score: Float,
    val message: String
)

data class CountryAdvisory(
    val iso_alpha2: String,
    val name: String,
    val continent: String,
    val advisory: Advisory
)

data class TravelAdvisoryResponse(
    val data: Map<String, CountryAdvisory>
)
