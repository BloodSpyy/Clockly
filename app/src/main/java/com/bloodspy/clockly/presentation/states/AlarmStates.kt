package com.bloodspy.clockly.presentation.states

sealed class AlarmStates {
    data object Initial: AlarmStates()

    data object Loading: AlarmStates()

    data object Success: AlarmStates()

    class DataLoaded(val hour: Int, val minute: Int): AlarmStates()
}