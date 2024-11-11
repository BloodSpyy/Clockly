package com.bloodspy.clockly.presentation.viewmodels

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.domain.usecases.CancelAlarmUseCase
import com.bloodspy.clockly.domain.usecases.DeleteAlarmUseCase
import com.bloodspy.clockly.domain.usecases.EditAlarmUseCase
import com.bloodspy.clockly.domain.usecases.GetAlarmsUseCase
import com.bloodspy.clockly.domain.usecases.GetNearestAlarmTimeUseCase
import com.bloodspy.clockly.domain.usecases.ScheduleAlarmUseCase
import com.bloodspy.clockly.helpers.AlarmTimeHelper
import com.bloodspy.clockly.helpers.TimeHelper
import com.bloodspy.clockly.presentation.states.AlarmsStates
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AlarmsViewModel @Inject constructor(
    private val getAlarmsUseCase: GetAlarmsUseCase,
    private val getNearestAlarmTimeUseCase: GetNearestAlarmTimeUseCase,
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
    private val cancelAlarmUseCase: CancelAlarmUseCase,
    private val editAlarmUseCase: EditAlarmUseCase,
    private val scheduleAlarmUseCase: ScheduleAlarmUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<AlarmsStates>(AlarmsStates.Initial)
    val state = _state.asStateFlow()

    private var countDownTimer: CountDownTimer? = null

    fun loadAlarms() {
        viewModelScope.launch {
            getAlarmsUseCase().collect {
                _state.value = AlarmsStates.AlarmsLoaded(it)
            }
        }
    }

    fun loadNearestAlarmTime() {
        viewModelScope.launch {
            getNearestAlarmTimeUseCase().collect { nearestAlarmTime ->
                countDownTimer?.cancel()

                if (nearestAlarmTime == null) {
                    _state.value = AlarmsStates.NearestAlarmTimeLoaded(null)
                }

                nearestAlarmTime?.let {
                    val timeToStartAlarm = AlarmTimeHelper.getTimeToStartAlarm(it)

                    _state.value = AlarmsStates.NearestAlarmTimeLoaded(
                        AlarmTimeHelper.parseTimeToStartAlarm(timeToStartAlarm)
                    )

                    // wait to new minute for start count down
                    delay(
                        TimeHelper.MILLIS_IN_MINUTE -
                                (System.currentTimeMillis() % TimeHelper.MILLIS_IN_MINUTE)
                    )

                    startCountDown(timeToStartAlarm)
                }
            }
        }
    }

    fun changeEnableState(alarmEntity: AlarmEntity) {
        viewModelScope.launch {
            val isActive = alarmEntity.isActive
            val validatedAlarmTime = AlarmTimeHelper.validateAlarmTime(
                alarmEntity.alarmTime
            )

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
                    AlarmTimeHelper.parseTimeToStartAlarm(
                        AlarmTimeHelper.getTimeToStartAlarm(validatedAlarmTime)
                    )
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

    private fun startCountDown(timeToStartAlarm: Long) {
        countDownTimer = object : CountDownTimer(timeToStartAlarm, TimeHelper.MILLIS_IN_MINUTE) {
            override fun onTick(p0: Long) {
                _state.value = AlarmsStates.NearestAlarmTimeLoaded(
                    AlarmTimeHelper.parseTimeToStartAlarm(p0)
                )
            }

            override fun onFinish() {
                _state.value = AlarmsStates.NearestAlarmTimeLoaded(null)
            }
        }.start()
    }
}