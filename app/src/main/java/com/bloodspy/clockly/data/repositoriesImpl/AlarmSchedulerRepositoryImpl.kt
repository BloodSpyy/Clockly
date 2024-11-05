package com.bloodspy.clockly.data.repositoriesImpl

import android.app.AlarmManager
import android.app.Application
import android.content.Context.ALARM_SERVICE
import com.bloodspy.clockly.domain.repositories.AlarmSchedulerRepository
import com.bloodspy.clockly.helpers.AlarmServiceHelper.getStartAlarmServicePendingIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlarmSchedulerRepositoryImpl @Inject constructor(
    private val application: Application,
) : AlarmSchedulerRepository {
    private val alarmManager = application.getSystemService(ALARM_SERVICE) as AlarmManager

    override fun scheduleAlarm(alarmId: Int, timeInMillis: Long) {
        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(timeInMillis, null),
            getStartAlarmServicePendingIntent(application, alarmId)
        )
    }

    override fun cancelAlarm(alarmId: Int) {
        alarmManager.cancel(getStartAlarmServicePendingIntent(application, alarmId))
    }
}