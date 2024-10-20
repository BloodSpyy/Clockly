package com.bloodspy.clockly.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


fun parseTimeToString(timeInMillis: Long): String {
    val timeInDate = Date(timeInMillis)

    val simpleDataFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    return simpleDataFormat.format(timeInDate)
}

<<<<<<< HEAD
fun parseTimeToLong(time: String): Long {

    val simpleDataFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    return simpleDataFormat.parse(time)?.time ?: 0L
}

fun calculateHour(timeInMillis: Long): Int {
    val Date
}
=======
fun getMillisFromAlarmTime(hour: Int, minute: Int): Long {
    val calendar = Calendar.getInstance()

    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)

    return calendar.timeInMillis
}



>>>>>>> presentation_layer
