package com.bloodspy.clockly.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.domain.usecases.AddAlarmUseCase
import com.bloodspy.clockly.domain.usecases.EditAlarmUseCase
import com.bloodspy.clockly.domain.usecases.GetAlarmUseCase
import com.bloodspy.clockly.domain.usecases.ScheduleAlarmUseCase
import com.bloodspy.clockly.helpers.TimeHelper
import com.bloodspy.clockly.presentation.states.AlarmStates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

class AlarmViewModel @Inject constructor(
    private val getAlarmUseCase: GetAlarmUseCase,
    private val addAlarmUseCase: AddAlarmUseCase,
    private val editAlarmUseCase: EditAlarmUseCase,
    private val scheduleAlarmUseCase: ScheduleAlarmUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<AlarmStates>(AlarmStates.Initial)
    val state = _state.asStateFlow()

    fun getAlarm(alarmId: Int) {
        _state.value = AlarmStates.Loading

        viewModelScope.launch {
            val alarmTime =
                getAlarmUseCase(alarmId)?.alarmTime ?: Calendar.getInstance().timeInMillis

            val (hour, minute) = TimeHelper.getHourAndMinuteFromTime(alarmTime)

            _state.value = AlarmStates.AlarmTimeLoaded(hour, minute)
        }
    }

    fun getTimeToAlarm(hour: Int, minute: Int) {
        _state.value = AlarmStates.Loading

        val validatedTimeInMillis = TimeHelper.validateTime(
            TimeHelper.getMillisFromTime(hour, minute)
        )

        _state.value = AlarmStates.TimeToAlarmLoaded(
            TimeHelper.getParsedTimeToStart(validatedTimeInMillis)
        )
    }

    fun addAlarm(hour: Int, minute: Int) {
        _state.value = AlarmStates.Loading

        val alarm = AlarmEntity(
            alarmTime = TimeHelper.validateTime(
                TimeHelper.getMillisFromTime(hour, minute)
            )
        )

        viewModelScope.launch {
            val alarmId = addAlarmUseCase(alarm).toInt()

            scheduleAlarmUseCase(alarmId, alarm.alarmTime)

            _state.value = AlarmStates.Success(
                TimeHelper.getParsedTimeToStart(alarm.alarmTime)
            )
        }
    }

    fun editAlarm(alarmId: Int, hour: Int, minute: Int) {
        _state.value = AlarmStates.Loading

        val alarm = AlarmEntity(
            id = alarmId,
            alarmTime = TimeHelper.validateTime(
                TimeHelper.getMillisFromTime(hour, minute)
            )
        )

        viewModelScope.launch {
            editAlarmUseCase(alarm)

            scheduleAlarmUseCase(alarmId, alarm.alarmTime)

            _state.value = AlarmStates.Success(
                TimeHelper.getParsedTimeToStart(alarm.alarmTime)
            )
        }
    }
}