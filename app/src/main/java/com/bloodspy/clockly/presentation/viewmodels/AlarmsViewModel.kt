package com.bloodspy.clockly.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.domain.usecases.AddAlarmUseCase
import com.bloodspy.clockly.domain.usecases.CancelAlarmUseCase
import com.bloodspy.clockly.domain.usecases.DeleteAlarmUseCase
import com.bloodspy.clockly.domain.usecases.EditAlarmUseCase
import com.bloodspy.clockly.domain.usecases.GetAlarmsUseCase
import com.bloodspy.clockly.domain.usecases.ScheduleAlarmUseCase
import com.bloodspy.clockly.utils.parseTime
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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