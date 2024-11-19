package com.bloodspy.clockly.presentation.viewmodels

import android.util.Log
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
            //todo тут я закостылил isOneAlarmTime
            val validatedTimeInMillis = TimeHelper.validateAlarmTime(
                getAlarmUseCase(alarmId)?.alarmTime ?: Calendar.getInstance().timeInMillis,
                true
            )

            _state.value = AlarmStates.DataLoaded(
                TimeHelper.getTimeParts(validatedTimeInMillis),
                TimeHelper.getParsedTimePartsToStart(validatedTimeInMillis)
            )
        }
    }

    fun updateTimeToStart(daysOfWeek: List<Int>, hour: Int, minute: Int) {
        val isOneTimeAlarm = (daysOfWeek.isEmpty())

        //todo объедини всё в один метод, если будешь такое часто использовать
        val validatedTimeInMillis = if (isOneTimeAlarm) {
            TimeHelper.validateAlarmTime(
                TimeHelper.getMillisFromTimeParts(null, hour, minute), isOneTimeAlarm
            )
        } else {
            daysOfWeek.minOf {
                TimeHelper.validateAlarmTime(
                    TimeHelper.getMillisFromTimeParts(it, hour, minute), isOneTimeAlarm
                )
            }
        }

        _state.value = AlarmStates.DataLoaded(
            TimeHelper.getTimeParts(validatedTimeInMillis),
            TimeHelper.getParsedTimePartsToStart(validatedTimeInMillis)
        )
    }

    fun addAlarm(hour: Int, minute: Int) {
        _state.value = AlarmStates.Loading

        val alarm = AlarmEntity(
            //todo тут я закостылил сегодняшний день + isOneTimeAlarm, так как дни ты ещё не учитываешь
            alarmTime = TimeHelper.validateAlarmTime(
                TimeHelper.getMillisFromTimeParts(
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                    hour,
                    minute
                ),
                false
            )
        )

        viewModelScope.launch {
            val alarmId = addAlarmUseCase(alarm).toInt()

            scheduleAlarmUseCase(alarmId, alarm.alarmTime)

            _state.value = AlarmStates.Success(
                TimeHelper.getParsedTimePartsToStart(alarm.alarmTime)
            )
        }
    }

    fun editAlarm(alarmId: Int, hour: Int, minute: Int) {
        _state.value = AlarmStates.Loading

        val alarm = AlarmEntity(
            id = alarmId,
            alarmTime = TimeHelper.validateAlarmTime(
                //todo тут я закостылил сегодняшнее число + isOneTImeALarm, так как дни ты ещё не учитываешь
                TimeHelper.getMillisFromTimeParts(
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                    hour,
                    minute
                ),
                false
            )
        )

        viewModelScope.launch {
            editAlarmUseCase(alarm)

            scheduleAlarmUseCase(alarmId, alarm.alarmTime)

            _state.value = AlarmStates.Success(
                TimeHelper.getParsedTimePartsToStart(alarm.alarmTime)
            )
        }
    }
}