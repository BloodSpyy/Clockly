package com.bloodspy.clockly.domain.scheduler

interface AlarmScheduler {
    fun scheduleAlarm(timeInMillis: Long)

    fun cancelAlarm()
}