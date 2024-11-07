package com.bloodspy.clockly.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


//todo попробуй вообще удалить этот файл
fun parseTime(timeInMillis: Long): String {
    val timeInDate = Date(timeInMillis)

    val simpleDataFormat = SimpleDateFormat("EEE:HH:mm", Locale.getDefault())

    Log.d("TimeUtils", simpleDataFormat.format(timeInDate))

    return simpleDataFormat.format(timeInDate)
}




