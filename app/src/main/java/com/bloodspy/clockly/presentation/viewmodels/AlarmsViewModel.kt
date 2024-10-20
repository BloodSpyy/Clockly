package com.bloodspy.clockly.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.domain.usecases.DeleteAlarmUseCase
import com.bloodspy.clockly.domain.usecases.EditAlarmUseCase
import com.bloodspy.clockly.domain.usecases.GetAlarmsUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlarmsViewModel @Inject constructor(
    getAlarmsUseCase: GetAlarmsUseCase,
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
    private val editAlarmUseCase: EditAlarmUseCase,
//    private val cancelAlarmUseCase: CancelAlarmUseCase,
//    private val scheduleAlarmUseCase: ScheduleAlarmUseCase
) : ViewModel() {

    val alarms = getAlarmsUseCase

    fun changeEnableState(alarmEntity: AlarmEntity) {
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
//        cancelAlarmUseCase()

        viewModelScope.launch {
            deleteAlarmUseCase(alarmEntity.id)
        }
    }
}