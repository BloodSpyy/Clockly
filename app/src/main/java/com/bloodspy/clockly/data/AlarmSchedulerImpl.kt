package com.bloodspy.clockly.data

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context.ALARM_SERVICE
import com.bloodspy.clockly.data.receiver.AlarmReceiver
import com.bloodspy.clockly.domain.scheduler.AlarmScheduler
import javax.inject.Inject

class AlarmSchedulerImpl @Inject constructor(
    private val application: Application,
) : AlarmScheduler {

    private val alarmManager = application.getSystemService(ALARM_SERVICE) as AlarmManager
    override fun scheduleAlarm(timeInMillis: Long) {
        val pendingIntent = PendingIntent.getBroadcast(
            application,
            100,
            AlarmReceiver.newIntent(application, timeInMillis),
            FLAG_IMMUTABLE
        )

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
            )
    }

    override fun cancelAlarm() {
        TODO("Not yet implemented")
    }
}