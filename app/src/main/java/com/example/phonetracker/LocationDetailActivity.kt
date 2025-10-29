package com.example.phonetracker

import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import androidx.appcompat.app.AppCompatActivity
import com.example.phonetracker.data.LocationData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Date

class LocationDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private var locationData: LocationData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail)

        // Get location data from intent
        val phoneNumber = intent.getStringExtra("phone_number") ?: ""
        val areaCode = intent.getStringExtra("area_code") ?: ""
        val city = intent.getStringExtra("city") ?: ""
        val address = intent.getStringExtra("address") ?: ""
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        val lastSeen = Date(intent.getLongExtra("last_seen", System.currentTimeMillis()))

        locationData = LocationData(
            phoneNumber = phoneNumber,
            areaCode = areaCode,
            city = city,
            address = address,
            latitude = latitude,
            longitude = longitude,
            lastSeen = lastSeen
        )

        // Set up map
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Set up UI
        setupUI()
    }

    private fun setupUI() {
        locationData?.let { location ->
            // Set phone number
            findViewById<android.widget.TextView>(R.id.phoneNumberText).text = 
                "+52 ${location.phoneNumber}"
            
            // Set city
            findViewById<android.widget.TextView>(R.id.cityText).text = 
                "Ciudad: ${location.city}"
            
            // Set address
            findViewById<android.widget.TextView>(R.id.addressText).text = 
                "Dirección: ${location.address}"
            
            // Set last seen
            val dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
            findViewById<android.widget.TextView>(R.id.lastSeenText).text = 
                "Última vez visto: ${dateFormat.format(location.lastSeen)}"
            
            // Set share button
            findViewById<android.widget.Button>(R.id.shareButton).setOnClickListener {
                shareLocation(location)
            }
        }
    }

    private fun shareLocation(location: LocationData) {
        val shareText = """
            Ubicación de +52 ${location.phoneNumber}:
            Ciudad: ${location.city}
            Dirección: ${location.address}
            Coordenadas: ${location.latitude}, ${location.longitude}
            Última vez visto: ${DateFormat.getDateTimeInstance().format(location.lastSeen)}
            
            Rastreado con Phone Tracker
        """.trimIndent()

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Compartir ubicación"))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        
        locationData?.let { location ->
            val position = LatLng(location.latitude, location.longitude)
            map.addMarker(MarkerOptions().position(position).title(location.address))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))
        }
    }
}