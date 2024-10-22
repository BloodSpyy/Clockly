package com.bloodspy.clockly.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


//todo попробуй вообще удалить этот файл
fun parseTime(timeInMillis: Long): String {
    val timeInDate = Date(timeInMillis)

    val simpleDataFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    return simpleDataFormat.format(timeInDate)
}




