package com.bloodspy.clockly.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun parseTimeToString(timeInMillis: Long): String {
    val timeInDate = Date(timeInMillis)

    val simpleDataFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    return simpleDataFormat.format(timeInDate)
}

fun parseTimeToLong(time: String): Long {

    val simpleDataFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    return simpleDataFormat.parse(time)?.time ?: 0L
}

fun calculateHour(timeInMillis: Long): Int {
    val Date
}