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
import kotlinx.coroutines.delay
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
            val alarm = getAlarmUseCase(alarmId)

            val alarmTimeInMillis = alarm?.alarmTime ?: Calendar.getInstance().timeInMillis

            val repeatingDays = alarm?.daysOfWeek?.split(",")?.map { it.toInt() }

            val validatedTimeToStartInMillis = repeatingDays?.let {
                TimeHelper.validateRepeatAlarm(alarmTimeInMillis)
            } ?: TimeHelper.validateOneTimeAlarm(alarmTimeInMillis)

            _state.value = AlarmStates.DataLoaded(
                TimeHelper.getHourAndMinuteFromAlarmTime(alarmTimeInMillis),
                TimeHelper.getParsedTimePartsToStart(validatedTimeToStartInMillis),
                repeatingDays
            )
        }
    }

    fun updateTimeToStart(daysOfWeek: List<Int>, hour: Int, minute: Int) {
        val validatedAlarmTimeInMillis = validateAlarmTime(daysOfWeek, hour, minute)

        _state.value = AlarmStates.DataLoaded(
            TimeHelper.getHourAndMinuteFromAlarmTime(validatedAlarmTimeInMillis),
            TimeHelper.getParsedTimePartsToStart(validatedAlarmTimeInMillis),
            daysOfWeek.takeIf { daysOfWeek.isNotEmpty() }
        )
    }

    fun addAlarm(daysOfWeek: List<Int>, hour: Int, minute: Int) {
        _state.value = AlarmStates.Loading

        val validatedAlarmTime = validateAlarmTime(daysOfWeek, hour, minute)

        val validatedDaysOfWeek = validateDaysOfWeek(daysOfWeek)

        val alarm = AlarmEntity(
            alarmTime = validatedAlarmTime,
            daysOfWeek = validatedDaysOfWeek
        )

        viewModelScope.launch {
            val alarmId = addAlarmUseCase(alarm).toInt()

            scheduleAlarmUseCase(alarmId, alarm.alarmTime)

            _state.value = AlarmStates.Success(
                TimeHelper.getParsedTimePartsToStart(alarm.alarmTime)
            )
        }
    }

    fun editAlarm(alarmId: Int, daysOfWeek: List<Int>, hour: Int, minute: Int) {
        _state.value = AlarmStates.Loading

        val validatedAlarmTime = validateAlarmTime(daysOfWeek, hour, minute)

        val validatedDaysOfWeek = validateDaysOfWeek(daysOfWeek)

        val alarm = AlarmEntity(
            id = alarmId,
            alarmTime = validatedAlarmTime,
            daysOfWeek = validatedDaysOfWeek
        )

        viewModelScope.launch {
            editAlarmUseCase(alarm)

            scheduleAlarmUseCase(alarmId, alarm.alarmTime)

            _state.value = AlarmStates.Success(
                TimeHelper.getParsedTimePartsToStart(alarm.alarmTime)
            )
        }
    }

    private fun validateDaysOfWeek(daysOfWeek: List<Int>): String? {
        return daysOfWeek
            .takeIf { daysOfWeek.isNotEmpty() }
            ?.joinToString(",")
    }

    private fun validateAlarmTime(
        daysOfWeek: List<Int>,
        hour: Int,
        minute: Int,
    ): Long {
        // if daysOfWeek.isEmpty -> one time alarm
        return if (daysOfWeek.isEmpty()) {
            getValidatedOneTimeAlarmTime(hour, minute)
        } else {
            getValidatedNearestRepeatAlarmTime(daysOfWeek, hour, minute)
        }
    }

    private fun getValidatedOneTimeAlarmTime(hour: Int, minute: Int): Long {
        return TimeHelper.validateOneTimeAlarm(
            TimeHelper.getMillisFromTimeParts(null, hour, minute)
        )
    }

    private fun getValidatedNearestRepeatAlarmTime(
        daysOfWeek: List<Int>,
        hour: Int,
        minute: Int,
    ): Long {
        return daysOfWeek.minOf {
            TimeHelper.validateRepeatAlarm(
                TimeHelper.getMillisFromTimeParts(it, hour, minute)
            )
        }
    }
}