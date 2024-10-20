package com.bloodspy.clockly.presentation.states

import com.bloodspy.clockly.domain.entities.AlarmEntity

sealed class AlarmStates {
    data object Initial: AlarmStates()

    data object Loading: AlarmStates()

    data object Success: AlarmStates()

    class DataLoaded(val alarmEntity: AlarmEntity): AlarmStates()
}