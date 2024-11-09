package com.bloodspy.clockly.presentation.states

sealed class AlarmStates {
    data object Initial: AlarmStates()

    data object Loading: AlarmStates()

    class DataLoaded(val hour: Int, val minute: Int): AlarmStates()

    class Success(val timeToAlarm: Array<Int>): AlarmStates()
}