package com.bloodspy.clockly.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.bloodspy.clockly.domain.usecases.AddAlarmUseCase
import com.bloodspy.clockly.domain.usecases.EditAlarmUseCase
import com.bloodspy.clockly.domain.usecases.GetAlarmUseCase
import com.bloodspy.clockly.domain.usecases.ScheduleAlarmUseCase
import com.bloodspy.clockly.utils.getHoursFromMillis
import com.bloodspy.clockly.utils.getMinutesFromMillis
import javax.inject.Inject

class AlarmViewModel @Inject constructor(
    getAlarmUseCase: GetAlarmUseCase,
    private val addAlarmUseCase: AddAlarmUseCase,
    private val editAlarmUseCase: EditAlarmUseCase,
    private val scheduleAlarmUseCase: ScheduleAlarmUseCase
): ViewModel() {
    val alarm = getAlarmUseCase

    fun getHours(timeInMillis: Long): Int = getHoursFromMillis(timeInMillis)

    fun getMinutes(timeInMillis: Long): Int = getMinutesFromMillis(timeInMillis)

}