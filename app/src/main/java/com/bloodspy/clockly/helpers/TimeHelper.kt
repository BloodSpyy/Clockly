package com.bloodspy.clockly.helpers

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object TimeHelper {
    const val MILLIS_IN_SECOND = 1000L
    const val MILLIS_IN_MINUTE = MILLIS_IN_SECOND * 60
    const val MILLIS_IN_HOUR = MILLIS_IN_MINUTE * 60
    const val MILLIS_IN_DAY = MILLIS_IN_HOUR * 24

    fun validateTime(timeInMillis: Long): Long {
        // if input time <= current time -> add 1 day
        return if (timeInMillis <= Calendar.getInstance().timeInMillis) {
            addDayToAlarmTime(timeInMillis)
        } else {
            timeInMillis
        }
    }

    fun getParsedTime(timeInMillis: Long, pattern: String = "HH:mm"): String {
        val timeInDate = Date(timeInMillis)

        val simpleDataFormat = SimpleDateFormat(pattern, Locale.getDefault())

        return simpleDataFormat.format(timeInDate)
    }

    fun getFormattedTimeToStart(declinations: Array<Array<String>>, timeTo: Array<Int>): String {
        return buildString {
            for (i in timeTo.indices) {
                val partOfTime = timeTo[i]

                if (partOfTime > 0) {
                    append(
                        getTimeDeclination(declinations[i], partOfTime)
                    )
                }
            }
        }.trim()
    }

    fun getParsedTimePartsToStart(timeInMillis: Long): Array<Int> {
        val timeToStartAlarm = getTimeToStart(timeInMillis)

        val days = (timeToStartAlarm / MILLIS_IN_DAY).toInt()
        val hours = ((timeToStartAlarm / MILLIS_IN_HOUR) % 24).toInt()
        val minutes = ((timeToStartAlarm / MILLIS_IN_MINUTE) % 60).toInt()

        return arrayOf(days, hours, minutes)
    }

    fun getMillisFromTimeParts(day: Int, hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        calendar.clear(Calendar.SECOND)
        calendar.clear(Calendar.MILLISECOND)

        return calendar.timeInMillis
    }

    fun getTimeParts(timeInMillis: Long): Array<Int> {
        val calendar = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis
        }

        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return arrayOf(day, hour, minute)
    }

    private fun getTimeToStart(timeInMillis: Long): Long {
        val alarmTimeInMillis = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis

            clear(Calendar.SECOND)
            clear(Calendar.MILLISECOND)
        }.timeInMillis

        val currentAlarmTimeInMillis = Calendar.getInstance().apply {
            clear(Calendar.SECOND)
            clear(Calendar.MILLISECOND)
        }.timeInMillis

        return alarmTimeInMillis - currentAlarmTimeInMillis
    }

    private fun getTimeDeclination(declination: Array<String>, time: Int): String {
        val preLastDigit = time % 100 / 10
        val lastDigit = time % 10

        if (preLastDigit == 1) {
            return "$time ${declination[1]} "
        }

        return when (lastDigit) {
            1 -> "$time ${declination[0]} "
            2, 3, 4 -> "$time ${declination[2]} "
            else -> "$time ${declination[1]} "
        }
    }

    private fun addDayToAlarmTime(timeInMillis: Long): Long {
        return Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis
            add(Calendar.DAY_OF_MONTH, 1)
        }.timeInMillis
    }
}