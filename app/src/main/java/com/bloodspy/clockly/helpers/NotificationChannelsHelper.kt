package com.bloodspy.clockly.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.bloodspy.clockly.R

object NotificationChannelsHelper {
    const val ALARM_NOTIFICATION_CHANNEL_ID = "1"

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(
                NotificationManager::class.java
            )

            val notificationChannels = mutableListOf<NotificationChannel>()

            createAlarmNotificationChannel(notificationChannels, context)

            notificationManager.createNotificationChannels(notificationChannels)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createAlarmNotificationChannel(
        notificationChannels: MutableList<NotificationChannel>,
        context: Context,
    ) {
        val alarmNotificationChannel = NotificationChannel(
            ALARM_NOTIFICATION_CHANNEL_ID,
            context.getString(R.string.alarm_notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setSound(null, null)
        }

        notificationChannels.add(alarmNotificationChannel)
    }
}