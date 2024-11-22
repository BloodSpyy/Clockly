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

    const val DAYS_IN_WEEK = 7

    fun validateOneTimeAlarm(timeInMillis: Long): Long {
        return if (timeInMillis <= Calendar.getInstance().timeInMillis) {
            addUnitOfTimeToAlarmTime(timeInMillis, Calendar.DAY_OF_MONTH)
        } else {
            timeInMillis
        }
    }

    fun validateRepeatAlarm(timeInMillis: Long): Long {
        return if (timeInMillis <= Calendar.getInstance().timeInMillis) {
            addUnitOfTimeToAlarmTime(timeInMillis, Calendar.WEEK_OF_MONTH)
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

    fun getMillisFromTimeParts(day: Int?, hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()

        if (day != null) {
            calendar.set(Calendar.DAY_OF_WEEK, day)
        }

        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        calendar.clear(Calendar.SECOND)
        calendar.clear(Calendar.MILLISECOND)

        return calendar.timeInMillis
    }

    fun getHourAndMinuteFromAlarmTime(timeInMillis: Long): Pair<Int, Int> {
        val calendar = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis
        }

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return Pair(hour, minute)
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

    private fun addUnitOfTimeToAlarmTime(timeInMillis: Long, unitOfTime: Int): Long {
        return Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis
            add(unitOfTime, 1)
        }.timeInMillis
    }

}