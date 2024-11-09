package com.bloodspy.clockly.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.domain.usecases.CancelAlarmUseCase
import com.bloodspy.clockly.domain.usecases.DeleteAlarmUseCase
import com.bloodspy.clockly.domain.usecases.EditAlarmUseCase
import com.bloodspy.clockly.domain.usecases.GetAlarmsUseCase
import com.bloodspy.clockly.domain.usecases.ScheduleAlarmUseCase
import com.bloodspy.clockly.helpers.AlarmTimeHelper
import com.bloodspy.clockly.presentation.states.AlarmsStates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlarmsViewModel @Inject constructor(
    private val getAlarmsUseCase: GetAlarmsUseCase,
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
    private val cancelAlarmUseCase: CancelAlarmUseCase,
    private val editAlarmUseCase: EditAlarmUseCase,
    private val scheduleAlarmUseCase: ScheduleAlarmUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<AlarmsStates>(AlarmsStates.Initial)
    val state = _state.asStateFlow()

    fun loadAlarms() {
        viewModelScope.launch {
            getAlarmsUseCase().collect {
                _state.value = AlarmsStates.AlarmsLoaded(it)
            }
        }
    }

    fun changeEnableState(alarmEntity: AlarmEntity) {
        viewModelScope.launch {
            val isActive = alarmEntity.isActive
            val validatedAlarmTime = AlarmTimeHelper.validateAlarmTime(
                alarmEntity.alarmTime
            )

            val newAlarmEntity = alarmEntity.copy(
                alarmTime = validatedAlarmTime,
                isActive = !isActive
            )

            editAlarmUseCase(newAlarmEntity)

            if (isActive) {
                cancelAlarmUseCase(alarmEntity.id)
            } else {
                scheduleAlarmUseCase(alarmEntity.id, validatedAlarmTime)

                _state.value = AlarmsStates.EditSuccess(
                    with(AlarmTimeHelper) {
                        parseTimeToStartAlarm(
                            getTimeToStartAlarm(validatedAlarmTime)
                        )
                    }
                )
            }
        }
    }

    fun deleteAlarm(alarmEntity: AlarmEntity) {
        viewModelScope.launch {
            deleteAlarmUseCase(alarmEntity.id)

            if (alarmEntity.isActive) {
                cancelAlarmUseCase(alarmEntity.id)
            }
        }
    }
}