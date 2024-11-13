package com.bloodspy.clockly.presentation.states

import com.bloodspy.clockly.domain.entities.AlarmEntity

sealed class AlarmsStates {
    data object Initial : AlarmsStates()

    data class DataLoaded(
        val alarms: List<AlarmEntity>,
        val timeToNearestAlarm: Array<Int>?,
    ) : AlarmsStates()

    data class EditSuccess(val timeToAlarm: Array<Int>) : AlarmsStates()
}