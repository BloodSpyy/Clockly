package com.bloodspy.clockly.presentation.states

sealed class AlarmStates {
    data object Initial : AlarmStates()

    data object Loading : AlarmStates()

    class AlarmTimeLoaded(val hour: Int, val minute: Int) : AlarmStates()

    class TimeToAlarmLoaded(val timeToAlarm: Array<Int>): AlarmStates()

    class Success(val timeToAlarm: Array<Int>) : AlarmStates()
}