package com.example.phonetracker.data

import java.util.Date

data class LocationData(
    val phoneNumber: String,
    val areaCode: String,
    val city: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val lastSeen: Date = Date()
)

data class CityData(
    val areaCode: String,
    val cityName: String,
    val addresses: List<AddressData>
)

data class AddressData(
    val street: String,
    val latitude: Double,
    val longitude: Double
)