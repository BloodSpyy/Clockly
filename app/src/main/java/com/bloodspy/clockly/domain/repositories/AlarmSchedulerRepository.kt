package com.bloodspy.clockly.domain.repositories

interface AlarmSchedulerRepository {
    fun schedule(alarmId: Int, timeInMillis: Long)

    fun cancel(alarmId: Int)
}