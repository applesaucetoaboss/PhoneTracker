package com.example.whatsapponlineviewer.util

import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class DateUtils {
    
    fun getRandomLastSeen(): String {
        val calendar = Calendar.getInstance()
        val now = calendar.timeInMillis
        
        // Generate random time within the last 24 hours
        val randomTimeInPast = Random.nextLong(0, 24 * 60 * 60 * 1000)
        val randomTime = now - randomTimeInPast
        
        calendar.timeInMillis = randomTime
        
        return formatLastSeen(calendar)
    }
    
    private fun formatLastSeen(calendar: Calendar): String {
        val now = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val fullDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        
        return when {
            // Today
            isSameDay(calendar, now) -> {
                "Hoy a las ${dateFormat.format(calendar.time)}"
            }
            // Yesterday
            isYesterday(calendar, now) -> {
                "Ayer a las ${dateFormat.format(calendar.time)}"
            }
            // This week
            isSameWeek(calendar, now) -> {
                val dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.MONDAY -> "Lunes"
                    Calendar.TUESDAY -> "Martes"
                    Calendar.WEDNESDAY -> "Miércoles"
                    Calendar.THURSDAY -> "Jueves"
                    Calendar.FRIDAY -> "Viernes"
                    Calendar.SATURDAY -> "Sábado"
                    Calendar.SUNDAY -> "Domingo"
                    else -> ""
                }
                "$dayOfWeek a las ${dateFormat.format(calendar.time)}"
            }
            // Older
            else -> {
                fullDateFormat.format(calendar.time)
            }
        }
    }
    
    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }
    
    private fun isYesterday(cal1: Calendar, cal2: Calendar): Boolean {
        val yesterday = Calendar.getInstance().apply {
            timeInMillis = cal2.timeInMillis
            add(Calendar.DAY_OF_YEAR, -1)
        }
        return isSameDay(cal1, yesterday)
    }
    
    private fun isSameWeek(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)
    }
}