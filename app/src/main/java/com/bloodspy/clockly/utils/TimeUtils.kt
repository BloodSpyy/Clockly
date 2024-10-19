package com.bloodspy.clockly.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun parseTime(timeInMillis: Long): String {
    val timeInDate = Date(timeInMillis)

    val simpleDataFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    return simpleDataFormat.format(timeInDate)
}

fun getHoursFromMillis(timeInMillis: Long): Int = (timeInMillis / 3_600_000).toInt()

fun getMinutesFromMillis(timeInMillis: Long): Int = ((timeInMillis / 60_000) % 60).toInt()
