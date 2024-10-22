package com.bloodspy.clockly.presentation.states

import com.bloodspy.clockly.domain.entities.AlarmEntity

sealed class AlarmsStates {
    data object Initial: AlarmsStates()

    data object Loading: AlarmsStates()

    class DataLoaded(val alarms: List<AlarmEntity>): AlarmsStates()
}