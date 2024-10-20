package com.bloodspy.clockly.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.domain.usecases.AddAlarmUseCase
import com.bloodspy.clockly.domain.usecases.EditAlarmUseCase
import com.bloodspy.clockly.domain.usecases.GetAlarmUseCase
import com.bloodspy.clockly.domain.usecases.ScheduleAlarmUseCase
import com.bloodspy.clockly.presentation.states.AlarmStates
import com.bloodspy.clockly.utils.getMillisFromAlarmTime
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

class AlarmViewModel @Inject constructor(
    private val getAlarmUseCase: GetAlarmUseCase,
    private val addAlarmUseCase: AddAlarmUseCase,
    private val scheduleAlarmUseCase: ScheduleAlarmUseCase
): ViewModel() {
    private val _state = MutableStateFlow<AlarmStates>(AlarmStates.Initial)
    val state = _state.asStateFlow()

    fun getAlarm(alarmId: Int) {
        _state.value = AlarmStates.Loading

        viewModelScope.launch {
            val alarm = getAlarmUseCase(alarmId)

            _state.value = AlarmStates.DataLoaded(alarm)
        }
    }

    //todo разберись, нужны ли в проекте add и edit методы одновременно
    fun addAlarm(hour: Int, minute: Int) {
        _state.value = AlarmStates.Loading

        val alarmTime = getMillisFromAlarmTime(hour, minute)

        val alarm = AlarmEntity(

        )

        viewModelScope.launch {
            addAlarmUseCase(
            )

            _state.value = AlarmStates.Success
        }
    }


}