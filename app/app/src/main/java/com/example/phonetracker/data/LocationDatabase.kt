package com.example.phonetracker.data

import android.content.Context
import android.content.SharedPreferences
import java.util.Date
import kotlin.random.Random

class LocationDatabase(context: Context) {
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences("location_prefs", Context.MODE_PRIVATE)
    
    // Track free usage
    fun getRemainingTracks(): Int {
        return sharedPreferences.getInt("remaining_tracks", 1)
    }
    
    fun decrementRemainingTracks() {
        val remaining = getRemainingTracks()
        if (remaining > 0) {
            sharedPreferences.edit().putInt("remaining_tracks", remaining - 1).apply()
        }
    }
    
    fun resetDailyTracks() {
        if (!isPremium()) {
            sharedPreferences.edit().putInt("remaining_tracks", 1).apply()
        }
    }
    
    // Premium status
    fun isPremium(): Boolean {
        return sharedPreferences.getBoolean("is_premium", false)
    }
    
    fun setPremium(isPremium: Boolean) {
        sharedPreferences.edit().putBoolean("is_premium", isPremium).apply()
    }
    
    // Mexican area codes database
    private val cityDatabase = listOf(
        CityData("55", "CDMX", generateAddresses("CDMX")),
        CityData("33", "Guadalajara", generateAddresses("Guadalajara")),
        CityData("81", "Monterrey", generateAddresses("Monterrey")),
        CityData("222", "Puebla", generateAddresses("Puebla")),
        CityData("664", "Tijuana", generateAddresses("Tijuana")),
        CityData("477", "León", generateAddresses("León")),
        CityData("656", "Cd. Juárez", generateAddresses("Cd. Juárez")),
        CityData("871", "Torreón", generateAddresses("Torreón")),
        CityData("444", "San Luis Potosí", generateAddresses("San Luis Potosí")),
        CityData("442", "Querétaro", generateAddresses("Querétaro"))
    )
    
    // Get location data based on phone number
    fun getLocationByPhoneNumber(phoneNumber: String): LocationData? {
        if (phoneNumber.length < 2) return null
        
        // Extract area code (first 2 or 3 digits)
        val areaCode = when {
            phoneNumber.startsWith("55") || phoneNumber.startsWith("33") || phoneNumber.startsWith("81") -> 
                phoneNumber.substring(0, 2)
            phoneNumber.startsWith("222") || phoneNumber.startsWith("664") || phoneNumber.startsWith("477") ||
            phoneNumber.startsWith("656") || phoneNumber.startsWith("871") || phoneNumber.startsWith("444") ||
            phoneNumber.startsWith("442") -> 
                phoneNumber.substring(0, 3)
            else -> return null
        }
        
        // Find city data
        val cityData = cityDatabase.find { it.areaCode == areaCode } ?: return null
        
        // Get random address from the city
        val randomAddress = cityData.addresses.random()
        
        return LocationData(
            phoneNumber = phoneNumber,
            areaCode = areaCode,
            city = cityData.cityName,
            address = randomAddress.street,
            latitude = randomAddress.latitude,
            longitude = randomAddress.longitude,
            lastSeen = Date()
        )
    }
    
    // Generate fake addresses for each city
    private fun generateAddresses(city: String): List<AddressData> {
        val addresses = mutableListOf<AddressData>()
        val random = Random(city.hashCode())
        
        // Base coordinates for each city (approximate center)
        val baseLat = when(city) {
            "CDMX" -> 19.4326
            "Guadalajara" -> 20.6597
            "Monterrey" -> 25.6866
            "Puebla" -> 19.0414
            "Tijuana" -> 32.5149
            "León" -> 21.1167
            "Cd. Juárez" -> 31.6904
            "Torreón" -> 25.5428
            "San Luis Potosí" -> 22.1565
            "Querétaro" -> 20.5881
            else -> 20.0000
        }
        
        val baseLng = when(city) {
            "CDMX" -> -99.1332
            "Guadalajara" -> -103.3496
            "Monterrey" -> -100.3161
            "Puebla" -> -98.2063
            "Tijuana" -> -117.0382
            "León" -> -101.6833
            "Cd. Juárez" -> -106.4245
            "Torreón" -> -103.4068
            "San Luis Potosí" -> -100.9855
            "Querétaro" -> -100.3899
            else -> -100.0000
        }
        
        // Street names for each city
        val streets = when(city) {
            "CDMX" -> listOf(
                "Av. Paseo de la Reforma", "Calle Madero", "Av. Insurgentes", 
                "Av. Chapultepec", "Calle Donceles", "Av. Revolución",
                "Calle Durango", "Av. Álvaro Obregón", "Calle Génova", "Av. Juárez"
            )
            "Guadalajara" -> listOf(
                "Av. Chapultepec", "Av. Vallarta", "Calle Independencia", 
                "Av. Hidalgo", "Av. López Mateos", "Av. México",
                "Calle Juárez", "Av. Américas", "Av. Federalismo", "Av. Revolución"
            )
            "Monterrey" -> listOf(
                "Av. Constitución", "Av. Gonzalitos", "Av. Revolución", 
                "Av. Eugenio Garza Sada", "Av. Paseo de los Leones", "Av. Lázaro Cárdenas",
                "Av. Fidel Velázquez", "Av. Lincoln", "Av. Ruiz Cortines", "Av. Sendero Divisorio"
            )
            "Puebla" -> listOf(
                "Av. Juárez", "Blvd. 5 de Mayo", "Av. Reforma", 
                "Calle 5 de Mayo", "Av. Zaragoza", "Av. 31 Oriente",
                "Blvd. Atlixco", "Av. 11 Sur", "Av. 43 Oriente", "Calle 2 Norte"
            )
            "Tijuana" -> listOf(
                "Av. Revolución", "Blvd. Agua Caliente", "Av. Constitución", 
                "Blvd. Díaz Ordaz", "Av. Paseo de los Héroes", "Blvd. Sánchez Taboada",
                "Av. Tecnológico", "Blvd. Insurgentes", "Av. Internacional", "Calle Primera"
            )
            else -> listOf(
                "Av. Principal", "Calle Central", "Blvd. Las Américas", 
                "Av. Hidalgo", "Calle Juárez", "Av. Independencia",
                "Calle Morelos", "Av. Revolución", "Blvd. Nacional", "Av. Constitución"
            )
        }
        
        // Generate 10 addresses for each city
        for (i in 0 until 10) {
            val streetName = streets[i]
            val streetNumber = random.nextInt(100, 999)
            val latOffset = random.nextDouble(-0.05, 0.05)
            val lngOffset = random.nextDouble(-0.05, 0.05)
            
            addresses.add(
                AddressData(
                    street = "$streetName #$streetNumber, $city",
                    latitude = baseLat + latOffset,
                    longitude = baseLng + lngOffset
                )
            )
        }
        
        return addresses
    }
}