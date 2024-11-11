package com.bloodspy.clockly.presentation.states

import com.bloodspy.clockly.domain.entities.AlarmEntity

sealed class AlarmsStates {
    data object Initial : AlarmsStates()

    class AlarmsLoaded(val alarms: List<AlarmEntity>) : AlarmsStates()

    class NearestAlarmTimeLoaded(val nearestAlarmTime: Array<Int>?): AlarmsStates()

    class EditSuccess(val timeToAlarm: Array<Int>) : AlarmsStates()
}