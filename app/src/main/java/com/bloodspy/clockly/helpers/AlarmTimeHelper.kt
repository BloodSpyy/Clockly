package com.bloodspy.clockly.helpers

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object AlarmTimeHelper {
    fun validateAlarmTime(alarmTime: Long): Long {
        // if input time <= current time -> add 1 day
        return if (alarmTime <= Calendar.getInstance().timeInMillis) {
            addDayToAlarmTime(alarmTime)
        } else {
            alarmTime
        }
    }

    //todo возможно придётся адаптировать под TimeHelper
    fun parseAlarmTime(timeInMillis: Long): String {
        val timeInDate = Date(timeInMillis)

        val simpleDataFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        return simpleDataFormat.format(timeInDate)
    }

    //todo замени числа на константы в TimeHelper
    fun parseTimeToStartAlarm(timeInMillis: Long): Array<Int> {
        val days = (timeInMillis / (1000 * 60 * 60 * 24)).toInt()
        val hours = ((timeInMillis / (1000 * 60 * 60)) % 24).toInt()
        val minutes = ((timeInMillis / (1000 * 60)) % 60).toInt()

        return arrayOf(days, hours, minutes)
    }

    fun getTimeToStartAlarm(timeInMillis: Long): Long {
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

    fun getFormattedTimeToStartAlarm(
        declinations: Array<Array<String>>,
        timeTo: Array<Int>,
    ): String {
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

    fun getMillisFromAlarmTime(hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()

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

    //todo замени числа на константы в TimeHelper
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

    private fun addDayToAlarmTime(alarmTime: Long): Long {
        return Calendar.getInstance().apply {
            timeInMillis = alarmTime
            add(Calendar.DAY_OF_MONTH, 1)
        }.timeInMillis
    }
}