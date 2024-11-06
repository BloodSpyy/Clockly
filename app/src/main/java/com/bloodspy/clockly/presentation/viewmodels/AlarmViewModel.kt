package com.bloodspy.clockly.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.domain.usecases.AddAlarmUseCase
import com.bloodspy.clockly.domain.usecases.EditAlarmUseCase
import com.bloodspy.clockly.domain.usecases.GetAlarmUseCase
import com.bloodspy.clockly.domain.usecases.ScheduleAlarmUseCase
import com.bloodspy.clockly.presentation.states.AlarmStates
import com.bloodspy.clockly.utils.parseTime
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

        if (alarmId == AlarmEntity.UNDEFINED_ID) {
            getHourAndMinuteFromAlarmTime(NO_SET_TIME).apply {
                _state.value = AlarmStates.DataLoaded(this.first, this.second)
            }
        } else {
            viewModelScope.launch {
                getHourAndMinuteFromAlarmTime(getAlarmUseCase(alarmId).alarmTime).apply {
                    _state.value = AlarmStates.DataLoaded(this.first, this.second)
                }
            }
        }
    }

    fun addAlarm(hour: Int, minute: Int) {
        _state.value = AlarmStates.Loading

        val alarm = AlarmEntity(
            alarmTime = validateAlarmTime(
                getMillisFromAlarmTime(hour, minute)
            )
        )

        viewModelScope.launch {
            val alarmId = addAlarmUseCase(alarm).toInt()

            scheduleAlarmUseCase(alarmId, alarm.alarmTime)

            _state.value = AlarmStates.Success
        }
    }

    fun editAlarm(alarmId: Int, hour: Int, minute: Int) {
        _state.value = AlarmStates.Loading

        val alarm = AlarmEntity(
            id = alarmId,
            alarmTime = getMillisFromAlarmTime(hour, minute)
        )

        viewModelScope.launch {
            editAlarmUseCase(alarm)

            scheduleAlarmUseCase(alarmId, alarm.alarmTime)

            _state.value = AlarmStates.Success
        }
    }

    private fun validateAlarmTime(alarmTime: Long): Long {
        // if input time <= current time -> add 1 day
        return if (alarmTime <= Calendar.getInstance().timeInMillis) {
            addDayToAlarmTime(alarmTime)
        } else {
            alarmTime
        }
    }

    private fun addDayToAlarmTime(alarmTime: Long): Long {
        return Calendar.getInstance().apply {
            timeInMillis = alarmTime
            add(Calendar.DAY_OF_MONTH, 1)
        }.timeInMillis
    }

    private fun getMillisFromAlarmTime(hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        calendar.clear(Calendar.SECOND)
        calendar.clear(Calendar.MILLISECOND)

        return calendar.timeInMillis
    }

    private fun getHourAndMinuteFromAlarmTime(timeInMillis: Long?): Pair<Int, Int> {
        val calendar = Calendar.getInstance()

        if (timeInMillis != null) {
            calendar.timeInMillis = timeInMillis
        }

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return Pair(hour, minute)
    }

    companion object {
        private val NO_SET_TIME = null
    }
}