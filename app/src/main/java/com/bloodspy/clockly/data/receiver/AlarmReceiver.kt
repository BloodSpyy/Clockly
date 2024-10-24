package com.bloodspy.clockly.data.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.bloodspy.clockly.R
import com.bloodspy.clockly.utils.parseTime

class AlarmReceiver : BroadcastReceiver() {
    private var timeInMillis: Long = DEFAULT_TIME_IN_MILLIS

    override fun onReceive(p0: Context?, p1: Intent?) {
        parseIntent(p1)

        p0?.let {
            val notificationManager = getNotificationManager(p0)

            createNotificationChannel(notificationManager)

            notificationManager.notify(ALARM_NOTIFICATION_ID, createAlarmNotification(p0))
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
            }

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createAlarmNotification(context: Context): Notification {
        with(context) {
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(
                    String.format(
                        getString(R.string.alarm_notification),
                        parseTime(timeInMillis)
                    )
                )
                .build()

            return notification
        }
    }

    private fun getNotificationManager(context: Context): NotificationManager = getSystemService(
        context,
        NotificationManager::class.java
    ) as NotificationManager

    private fun parseIntent(intent: Intent?) {
        if (intent == null) {
            throw RuntimeException("Alarm receiver intent is null")
        }

        with(intent) {
            if (!hasExtra(EXTRA_TIME_IN_MILLIS)) {
                throw RuntimeException("Param time in millis not found")
            }
            timeInMillis = getLongExtra(EXTRA_TIME_IN_MILLIS, DEFAULT_TIME_IN_MILLIS)
        }
    }

    companion object {
        private const val CHANNEL_ID = "100"
        private const val CHANNEL_NAME = "alarm"
        private const val CHANNEL_DESCRIPTION = "Alarm"

        private const val EXTRA_TIME_IN_MILLIS = "time"

        private const val ALARM_NOTIFICATION_ID = 1

        private const val DEFAULT_TIME_IN_MILLIS = 0L

        fun newIntent(context: Context, timeInMillis: Long): Intent {
            return Intent(context, AlarmReceiver::class.java).apply {
                putExtra(EXTRA_TIME_IN_MILLIS, timeInMillis)
            }
        }
    }
}

