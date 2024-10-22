package com.bloodspy.clockly.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.domain.usecases.DeleteAlarmUseCase
import com.bloodspy.clockly.domain.usecases.EditAlarmUseCase
import com.bloodspy.clockly.domain.usecases.GetAlarmsUseCase
import com.bloodspy.clockly.presentation.states.AlarmsStates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlarmsViewModel @Inject constructor(
    private val getAlarmsUseCase: GetAlarmsUseCase,
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
    private val editAlarmUseCase: EditAlarmUseCase,
//    private val cancelAlarmUseCase: CancelAlarmUseCase,
//    private val scheduleAlarmUseCase: ScheduleAlarmUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<AlarmsStates>(AlarmsStates.Initial)
    val state = _state.asStateFlow()

    fun getAlarms() {
        _state.value = AlarmsStates.Loading

        viewModelScope.launch {
            getAlarmsUseCase().collect {
                _state.value = AlarmsStates.DataLoaded(it)
            }
        }
    }

    fun changeEnableState(alarmEntity: AlarmEntity) {
        _state.value = AlarmsStates.Loading

        val isActive = alarmEntity.isActive

//        if(isActive) {
//            cancelAlarmUseCase()
//        } else {
//            scheduleAlarmUseCase(alarmEntity.alarmTime)
//        }

        viewModelScope.launch {
            val newAlarmEntity = alarmEntity.copy(isActive = !isActive)

            editAlarmUseCase(newAlarmEntity)
        }
    }

    fun deleteAlarm(alarmEntity: AlarmEntity) {
        _state.value = AlarmsStates.Loading
//        cancelAlarmUseCase()

        viewModelScope.launch {
            deleteAlarmUseCase(alarmEntity.id)
        }
    }
}