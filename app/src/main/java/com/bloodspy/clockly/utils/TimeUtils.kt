package com.bloodspy.clockly.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun parseTime(timeInMillis: Long): String {
    val timeInDate = Date(timeInMillis)

    val simpleDataFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    return simpleDataFormat.format(timeInDate)
}

fun getMillisFromAlarmTime(hour: Int, minute: Int): Long {
    val calendar = Calendar.getInstance()

    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)

    return calendar.timeInMillis
}



