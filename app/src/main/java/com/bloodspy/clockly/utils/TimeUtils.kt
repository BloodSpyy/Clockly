package com.bloodspy.clockly.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val calendar = Calendar.getInstance()

fun parseTime(timeInMillis: Long): String {
    val timeInDate = Date(timeInMillis)

    val simpleDataFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    return simpleDataFormat.format(timeInDate)
}

fun getMillisFromAlarmTime(hour: Int, minute: Int): Long {
    calendar.set(hour, Calendar.HOUR_OF_DAY)
    calendar.set(minute, Calendar.MINUTE)

    return calendar.timeInMillis
}


