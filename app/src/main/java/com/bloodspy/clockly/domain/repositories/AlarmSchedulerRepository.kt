package com.bloodspy.clockly.domain.repositories

interface AlarmSchedulerRepository {
    fun scheduleAlarm(alarmId: Int, timeInMillis: Long)

    fun cancelAlarm(alarmId: Int)
}