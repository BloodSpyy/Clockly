package com.bloodspy.clockly.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.domain.usecases.AddAlarmUseCase
import com.bloodspy.clockly.domain.usecases.CancelAlarmUseCase
import com.bloodspy.clockly.domain.usecases.DeleteAlarmUseCase
import com.bloodspy.clockly.domain.usecases.GetAlarmsUseCase
import com.bloodspy.clockly.domain.usecases.GetNearestAlarmTime
import com.bloodspy.clockly.domain.usecases.ScheduleAlarmUseCase
import com.bloodspy.clockly.presentation.states.AlarmStates
import com.bloodspy.clockly.presentation.states.AlarmsStates
import com.bloodspy.clockly.utils.parseTime
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlarmsViewModel @Inject constructor(
    private val getAlarmsUseCase: GetAlarmsUseCase,
    private val getNearestAlarmTime: GetNearestAlarmTime,
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
    private val cancelAlarmUseCase: CancelAlarmUseCase,
    private val addAlarmUseCase: AddAlarmUseCase,
    private val scheduleAlarmUseCase: ScheduleAlarmUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<AlarmsStates>(AlarmsStates.Initial)
    val state = _state.asStateFlow()

    suspend fun loadData() {
        _state.value = AlarmsStates.Loading

        viewModelScope.launch {

            val alarmsJob = launch {
                getAlarmsUseCase().collect {
                    Log.d("AlarmsViewModel", "in coroutine 1")
                    _state.value = AlarmsStates.AlarmsLoaded(it)
                }
            }

            val nearestAlarmJob = launch {
                _state.value = AlarmsStates.NearestAlarmLoaded(
                    getNearestAlarmTime()?.let {
                        Log.d("AlarmsViewModel", "in coroutine 2")
                        parseTime(it)
                    }
                )
            }

            alarmsJob.join()
            nearestAlarmJob.join()


            Log.d("AlarmsViewModel", "out coroutine")
        }
    }

    fun changeEnableState(alarmEntity: AlarmEntity) {
        _state.value = AlarmsStates.Loading

        viewModelScope.launch {
            val isActive = alarmEntity.isActive

            if (isActive) {
                cancelAlarmUseCase(alarmEntity.id)
            } else {
                scheduleAlarmUseCase(alarmEntity.id, alarmEntity.alarmTime)
            }

            val newAlarmEntity = alarmEntity.copy(isActive = !isActive)

            addAlarmUseCase(newAlarmEntity)

            _state.value = AlarmsStates.Success
        }
    }

    fun deleteAlarm(alarmEntity: AlarmEntity) {
        _state.value = AlarmsStates.Loading

        viewModelScope.launch {
            with(alarmEntity.id) {
                deleteAlarmUseCase(this)
                cancelAlarmUseCase(this)
            }

            _state.value = AlarmsStates.Success
        }
    }
}