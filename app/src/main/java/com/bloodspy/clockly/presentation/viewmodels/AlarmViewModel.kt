package com.bloodspy.clockly.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.domain.usecases.AddAlarmUseCase
import com.bloodspy.clockly.domain.usecases.GetAlarmUseCase
import com.bloodspy.clockly.domain.usecases.ScheduleAlarmUseCase
import com.bloodspy.clockly.presentation.states.AlarmStates
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
    private val _state = MutableStateFlow<AlarmStates>()
    val state = _state.asStateFlow()

    fun getAlarm(alarmId: Int) {
        _state.value = AlarmStates.Loading

        viewModelScope.launch {
            val alarm = getAlarmUseCase(alarmId)

            val alarmTime = getAlarmTimeFromMillis(alarm.alarmTime)

            _state.value = AlarmStates.DataLoaded(alarmTime.first, alarmTime.second)
        }
    }

    //todo разберись, нужны ли в проекте add и edit методы одновременно
    fun addAlarm(hour: Int, minute: Int) {
        _state.value = AlarmStates.Loading

        val alarm = AlarmEntity(
            alarmTime = getMillisFromAlarmTime(hour, minute)
        )

        viewModelScope.launch {
            addAlarmUseCase(alarm)

            _state.value = AlarmStates.Success
        }
    }



    private fun getMillisFromAlarmTime(hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        return calendar.timeInMillis
    }

    private fun getAlarmTimeFromMillis(timeInMillis: Long): Pair<Int, Int> {

    }

}