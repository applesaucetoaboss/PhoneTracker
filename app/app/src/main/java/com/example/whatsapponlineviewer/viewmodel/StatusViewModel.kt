package com.example.whatsapponlineviewer.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.whatsapponlineviewer.R
import com.example.whatsapponlineviewer.util.DateUtils
import java.util.*
import kotlin.random.Random

data class StatusResult(val status: String, val lastSeen: String)

class StatusViewModel(application: Application) : AndroidViewModel(application) {

    private val _statusResult = MutableLiveData<StatusResult>()
    val statusResult: LiveData<StatusResult> = _statusResult

    private val _remainingChecks = MutableLiveData<Int>()
    val remainingChecks: LiveData<Int> = _remainingChecks

    private val _isPremium = MutableLiveData<Boolean>()
    val isPremium: LiveData<Boolean> = _isPremium

    private val sharedPrefs = application.getSharedPreferences("whatsapp_tracker", Context.MODE_PRIVATE)
    private val dateUtils = DateUtils()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val isPremiumValue = sharedPrefs.getBoolean("is_premium", false)
        _isPremium.value = isPremiumValue

        val lastResetDate = sharedPrefs.getLong("last_reset_date", 0)
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        // Reset counter if it's a new day
        if (lastResetDate < today) {
            sharedPrefs.edit()
                .putInt("remaining_checks", 3)
                .putLong("last_reset_date", today)
                .apply()
        }

        _remainingChecks.value = sharedPrefs.getInt("remaining_checks", 3)
    }

    fun checkStatus(phoneNumber: String) {
        // Generate random status (online/offline)
        val isOnline = Random.nextBoolean()
        val status = if (isOnline) {
            getApplication<Application>().getString(R.string.online)
        } else {
            getApplication<Application>().getString(R.string.offline)
        }

        // Generate random last seen time within the last 24 hours
        val lastSeen = dateUtils.getRandomLastSeen()

        _statusResult.value = StatusResult(status, lastSeen)

        // Decrease remaining checks if not premium
        if (_isPremium.value != true) {
            val remaining = (_remainingChecks.value ?: 0) - 1
            _remainingChecks.value = remaining
            sharedPrefs.edit().putInt("remaining_checks", remaining).apply()
        }
    }

    fun canCheckStatus(): Boolean {
        return _isPremium.value == true || (_remainingChecks.value ?: 0) > 0
    }

    fun setPremium() {
        _isPremium.value = true
        sharedPrefs.edit().putBoolean("is_premium", true).apply()
    }
}