package com.cashcue.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateConverter {
    fun convertMillisToString(timeMillis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeMillis
        val sdf = SimpleDateFormat("EEE, dd/MM/yyyy", Locale.getDefault())
        return sdf.format(calendar.time)
    }

    fun convertToDayOfWeek(timeMillis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeMillis
        val sdf = SimpleDateFormat("EEE", Locale.getDefault())
        return sdf.format(calendar.time)
    }
}