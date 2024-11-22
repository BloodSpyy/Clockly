package com.bloodspy.clockly.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.domain.usecases.CancelAlarmUseCase
import com.bloodspy.clockly.domain.usecases.DeleteAlarmUseCase
import com.bloodspy.clockly.domain.usecases.EditAlarmUseCase
import com.bloodspy.clockly.domain.usecases.GetActivatedAlarmsUseCase
import com.bloodspy.clockly.domain.usecases.GetAlarmsFlowUseCase
import com.bloodspy.clockly.domain.usecases.ScheduleAlarmUseCase
import com.bloodspy.clockly.helpers.TimeHelper
import com.bloodspy.clockly.presentation.states.AlarmsStates
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

class AlarmsViewModel @Inject constructor(
    private val getAlarmsFlowUseCase: GetAlarmsFlowUseCase,
    private val getActivatedAlarmsUseCase: GetActivatedAlarmsUseCase,
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
    private val cancelAlarmUseCase: CancelAlarmUseCase,
    private val editAlarmUseCase: EditAlarmUseCase,
    private val scheduleAlarmUseCase: ScheduleAlarmUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<AlarmsStates>(AlarmsStates.Initial)
    val state = _state.asStateFlow()

    private var updateTimeJob: Job? = null

    fun validateActivatedAlarms() {
        viewModelScope.launch {
            val activatedAlarms = getActivatedAlarmsUseCase()

            activatedAlarms?.let {
                it.forEach { alarm ->
                    if (alarm.alarmTime >= System.currentTimeMillis()) {
                        scheduleAlarmUseCase(alarm.id, alarm.alarmTime)
                    }

                    if (alarm.alarmTime < System.currentTimeMillis()) {
                        val changedAlarm = if (alarm.daysOfWeek != null) {
                            handleRepeatingAlarm(alarm)
                        } else {
                            alarm.copy(isActive = false)
                        }

                        editAlarmUseCase(changedAlarm)
                    }
                }
            }
        }
    }

    fun loadData() {
        viewModelScope.launch {
            getAlarmsFlowUseCase().collect { alarms ->
                updateTimeJob?.cancel()

                val nearestAlarm = alarms.filter { it.isActive }
                    .minByOrNull { it.alarmTime }

                if (nearestAlarm == null) {
                    _state.value = AlarmsStates.DataLoaded(alarms, null)
                } else {
                    updateTimeJob = launch {
                        while (isActive) {
                            val timePartsToStart = TimeHelper.getParsedTimePartsToStart(
                                nearestAlarm.alarmTime
                            )

                            if (timePartsToStart.all { it == 0 }) {
                                break
                            }

                            _state.value = AlarmsStates.DataLoaded(
                                alarms,
                                timePartsToStart
                            )

                            // delayed until new minute for timely update
                            delay(
                                TimeHelper.MILLIS_IN_MINUTE -
                                        System.currentTimeMillis() % TimeHelper.MILLIS_IN_MINUTE
                            )
                        }
                    }
                }
            }
        }
    }

    fun changeEnableState(alarmEntity: AlarmEntity) {
        viewModelScope.launch {
            val isActive = alarmEntity.isActive

            // if days of week != null -> repeat alarm
            val validatedAlarmTime = alarmEntity.daysOfWeek?.let {
                TimeHelper.validateRepeatAlarm(alarmEntity.alarmTime)
            } ?: TimeHelper.validateOneTimeAlarm(alarmEntity.alarmTime)

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
                    TimeHelper.getParsedTimePartsToStart(validatedAlarmTime)
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

    private fun handleRepeatingAlarm(alarm: AlarmEntity): AlarmEntity {
        alarm.daysOfWeek?.let { daysOfWeek ->
            val newAlarmTime = getValidatedNearestRepeatAlarmTime(
                daysOfWeek, alarm.alarmTime
            )

            scheduleAlarmUseCase(alarm.id, newAlarmTime)

            return alarm.copy(alarmTime = newAlarmTime)
        } ?: throw RuntimeException("This method should be used only on repeating alarm")
    }

    private fun getValidatedNearestRepeatAlarmTime(
        daysOfWeek: String,
        timeInMillis: Long,
    ): Long {
        val calendar = Calendar.getInstance()

        val daysOfWeekList = daysOfWeek.split(",").map { it.toInt() }

        val validatedNearestRepeatAlarmTime = daysOfWeekList.minOf { day ->
            TimeHelper.validateRepeatAlarm(
                calendar.apply {
                    this.timeInMillis = timeInMillis
                    set(Calendar.DAY_OF_WEEK, day)
                }.timeInMillis
            )
        }

        return validatedNearestRepeatAlarmTime
    }
}