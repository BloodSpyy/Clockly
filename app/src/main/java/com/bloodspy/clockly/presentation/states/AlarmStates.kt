package com.bloodspy.clockly.presentation.states

sealed class AlarmStates {
    data object Initial : AlarmStates()

    data object Loading : AlarmStates()

    data class DataLoaded(
        val alarmTimeParts: Array<Int>,
        val timeToAlarmParts: Array<Int>,
        val repeatingDays: List<Int>?,
    ) : AlarmStates()

    data class Success(val timeToAlarm: Array<Int>) : AlarmStates()
}