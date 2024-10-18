package com.bloodspy.clockly.domain.usecases

import com.bloodspy.clockly.domain.scheduler.AlarmScheduler
import javax.inject.Inject

class ScheduleAlarmUseCase @Inject constructor(private val alarmScheduler: AlarmScheduler){
    operator fun invoke(timeInMillis: Long) {
        alarmScheduler.scheduleAlarm(timeInMillis)
    }
}