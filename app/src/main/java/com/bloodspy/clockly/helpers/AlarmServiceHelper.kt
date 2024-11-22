package com.bloodspy.clockly.helpers

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.os.Build
import com.bloodspy.clockly.services.AlarmService

object AlarmServiceHelper {
    fun getStartAlarmServicePendingIntent(
        context: Context,
        alarmId: Int,
    ): PendingIntent =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent.getForegroundService(
                context,
                alarmId,
                AlarmService.newIntentStartAlarm(context, alarmId),
                FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getService(
                context,
                alarmId,
                AlarmService.newIntentStartAlarm(context, alarmId),
                FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
            )
        }

    fun getStopAlarmServicePendingIntent(
        context: Context,
        alarmId: Int,
    ): PendingIntent =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent.getForegroundService(
                context,
                alarmId,
                AlarmService.newIntentStopAlarm(context, alarmId),
                FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getService(
                context,
                alarmId,
                AlarmService.newIntentStopAlarm(context, alarmId),
                FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
            )
        }
}