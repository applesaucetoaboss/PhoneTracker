package com.example.phonetracker

import android.app.Application
import com.example.phonetracker.data.LocationDatabase
import java.util.Calendar

class MyApplication : Application() {
    
    lateinit var locationDatabase: LocationDatabase
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize location database
        locationDatabase = LocationDatabase(this)
        
        // Reset daily free tracks at midnight
        checkAndResetDailyTracks()
    }
    
    private fun checkAndResetDailyTracks() {
        val lastResetDate = getSharedPreferences("app_prefs", MODE_PRIVATE)
            .getLong("last_reset_date", 0)
        
        val currentDate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        
        if (lastResetDate < currentDate) {
            // Reset daily tracks
            locationDatabase.resetDailyTracks()
            
            // Save current date as last reset date
            getSharedPreferences("app_prefs", MODE_PRIVATE).edit()
                .putLong("last_reset_date", currentDate)
                .apply()
        }
    }
}