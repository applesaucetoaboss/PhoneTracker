package com.example.phonetracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.phonetracker.data.LocationData
import com.example.phonetracker.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        updateRemainingTracksText()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupListeners() {
        binding.btnTrackPhone.setOnClickListener {
            val phoneNumber = binding.etPhoneNumber.text.toString()
            
            if (phoneNumber.length != 10) {
                Toast.makeText(this, getString(R.string.invalid_phone), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val app = application as MyApplication
            if (app.locationDatabase.getRemainingTracks() > 0 || app.locationDatabase.isPremium()) {
                trackPhone(phoneNumber)
            } else {
                showPremiumDialog()
            }
        }

        binding.btnPremium.setOnClickListener {
            startActivity(Intent(this, PaymentActivity::class.java))
        }
    }

    private fun trackPhone(phoneNumber: String) {
        // Show loading animation
        binding.progressBar.visibility = View.VISIBLE
        binding.tvSearching.visibility = View.VISIBLE
        binding.btnTrackPhone.isEnabled = false

        // Simulate network delay
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000) // 3 seconds delay
            
            val app = application as MyApplication
            val locationData = app.locationDatabase.getLocationByPhoneNumber(phoneNumber)
            
            if (locationData != null) {
                // Decrement remaining tracks if not premium
                if (!app.locationDatabase.isPremium()) {
                    app.locationDatabase.decrementRemainingTracks()
                    updateRemainingTracksText()
                }
                
                // Navigate to location detail screen
                showLocationDetail(locationData)
            } else {
                Toast.makeText(
                    this@MainActivity, 
                    getString(R.string.location_not_found), 
                    Toast.LENGTH_LONG
                ).show()
            }
            
            // Hide loading animation
            binding.progressBar.visibility = View.GONE
            binding.tvSearching.visibility = View.GONE
            binding.btnTrackPhone.isEnabled = true
        }
    }

    private fun showLocationDetail(locationData: LocationData) {
        val intent = Intent(this, LocationDetailActivity::class.java).apply {
            putExtra("phone_number", locationData.phoneNumber)
            putExtra("area_code", locationData.areaCode)
            putExtra("city", locationData.city)
            putExtra("address", locationData.address)
            putExtra("latitude", locationData.latitude)
            putExtra("longitude", locationData.longitude)
            putExtra("last_seen", locationData.lastSeen.time)
        }
        startActivity(intent)
    }

    private fun updateRemainingTracksText() {
        val app = application as MyApplication
        val remaining = app.locationDatabase.getRemainingTracks()
        
        if (app.locationDatabase.isPremium()) {
            binding.tvRemainingTracks.text = getString(R.string.premium_active)
        } else {
            binding.tvRemainingTracks.text = getString(R.string.remaining_tracks, remaining)
        }
    }

    private fun showPremiumDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.premium_required))
            .setMessage(getString(R.string.premium_description))
            .setPositiveButton(getString(R.string.upgrade_now)) { _, _ ->
                startActivity(Intent(this, PaymentActivity::class.java))
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
}