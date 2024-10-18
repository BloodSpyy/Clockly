package com.bloodspy.clockly.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun parseTime(timeInMillis: Long): String {
    val timeInDate = Date(timeInMillis)

    val simpleDataFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    return simpleDataFormat.format(timeInDate)
}