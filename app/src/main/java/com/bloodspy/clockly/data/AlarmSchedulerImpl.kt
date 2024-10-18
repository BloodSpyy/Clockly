package com.bloodspy.clockly.data

import android.app.AlarmManager
import android.app.Application
import android.content.Context.ALARM_SERVICE
import com.bloodspy.clockly.domain.scheduler.AlarmScheduler
import javax.inject.Inject

class AlarmSchedulerImpl @Inject constructor(
    private val application: Application
): AlarmScheduler {

    private val alarmManager = application.getSystemService(ALARM_SERVICE) as AlarmManager
    override fun scheduleAlarm(timeInMillis: Long) {
        TODO("Not yet implemented")
    }

    override fun cancelAlarm() {
        TODO("Not yet implemented")
    }
}