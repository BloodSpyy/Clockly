package com.bloodspy.clockly.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.domain.usecases.AddAlarmUseCase
import com.bloodspy.clockly.domain.usecases.DeleteAlarmUseCase
import com.bloodspy.clockly.domain.usecases.EditAlarmUseCase
import com.bloodspy.clockly.domain.usecases.GetAlarmsUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class AlarmsViewModel @Inject constructor(
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
    private val editAlarmUseCase: EditAlarmUseCase,
    getAlarmsUseCase: GetAlarmsUseCase,
) : ViewModel() {

    val alarms = getAlarmsUseCase

    fun changeEnableState(alarmEntity: AlarmEntity) {
        viewModelScope.launch {
            val newAlarmEntity = alarmEntity.copy(isActive = !alarmEntity.isActive)

            editAlarmUseCase(newAlarmEntity)
        }
    }

    fun deleteAlarm(alarmEntity: AlarmEntity) {
        viewModelScope.launch {
            deleteAlarmUseCase(alarmEntity.id)
        }
    }
}