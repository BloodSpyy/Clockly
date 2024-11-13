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
            val validatedTimeInMillis = TimeHelper.validateTime(
                getAlarmUseCase(alarmId)?.alarmTime ?: Calendar.getInstance().timeInMillis
            )

            _state.value = AlarmStates.DataLoaded(
                TimeHelper.getTimeParts(validatedTimeInMillis),
                TimeHelper.getParsedTimePartsToStart(validatedTimeInMillis)
            )
        }
    }

    fun updateTimePicker(hour: Int, minute: Int) {
        //todo тут я закостылил сегодняшний день, так как дни ты ещё не учитываешь
        val validatedTimeInMillis = TimeHelper.validateTime(
            TimeHelper.getMillisFromTimeParts(
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                hour,
                minute
            )
        )

        _state.value = AlarmStates.DataLoaded(
            TimeHelper.getTimeParts(validatedTimeInMillis),
            TimeHelper.getParsedTimePartsToStart(validatedTimeInMillis)
        )
    }

    fun addAlarm(hour: Int, minute: Int) {
        _state.value = AlarmStates.Loading

        val alarm = AlarmEntity(
            //todo тут я закостылил сегодняшний день, так как дни ты ещё не учитываешь
            alarmTime = TimeHelper.validateTime(
                TimeHelper.getMillisFromTimeParts(
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                    hour,
                    minute
                )
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
            alarmTime = TimeHelper.validateTime(
                //todo тут я закостылил сегодняшнее число, так как дни ты ещё не учитываешь
                TimeHelper.getMillisFromTimeParts(
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                    hour,
                    minute
                )
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