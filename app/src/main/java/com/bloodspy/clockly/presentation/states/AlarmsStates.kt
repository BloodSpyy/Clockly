package com.bloodspy.clockly.presentation.states

import com.bloodspy.clockly.domain.entities.AlarmEntity

sealed class AlarmsStates {
    data object Initial : AlarmsStates()

    data object Loading : AlarmsStates()

    class NearestAlarmLoaded(val nearestAlarm: String?) : AlarmsStates()

    class AlarmsLoaded(val alarms: List<AlarmEntity>) : AlarmsStates()
}