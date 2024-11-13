package com.bloodspy.clockly.presentation.states

sealed class AlarmStates {
    data object Initial : AlarmStates()

    data object Loading : AlarmStates()

    //todo объедини AlarmTimeLoaded и TimeToAlarmLoaded
    data class AlarmTimeLoaded(val hour: Int, val minute: Int) : AlarmStates()

    data class TimeToAlarmLoaded(val timeToAlarm: Array<Int>) : AlarmStates()

    data class Success(val timeToAlarm: Array<Int>) : AlarmStates()
}