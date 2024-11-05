package com.bloodspy.clockly.helpers

import android.app.Notification
import android.content.Context
import android.icu.util.Calendar
import androidx.core.app.NotificationCompat
import com.bloodspy.clockly.R
import com.bloodspy.clockly.helpers.NotificationChannelsHelper.ALARM_NOTIFICATION_CHANNEL_ID
import com.bloodspy.clockly.utils.parseTime

object AlarmNotificationHelper {
    const val ALARM_NOTIFICATION_ID = 1

    fun createAlarmNotification(context: Context, alarmId: Int): Notification {
        return with(context) {
            NotificationCompat.Builder(
                this,
                ALARM_NOTIFICATION_CHANNEL_ID
            )
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(
                    String.format(
                        getString(R.string.alarm_notification),
                        parseTime(Calendar.getInstance().timeInMillis)
                    )
                )
                .setAutoCancel(true)
                .addAction(
                    R.drawable.ic_launcher_background,
                    getString(R.string.alarm_notification_button),
                    AlarmServiceHelper.getStopAlarmServicePendingIntent(
                        context,
                        alarmId
                    )
                )
                .setDeleteIntent(
                    AlarmServiceHelper.getStopAlarmServicePendingIntent(
                        context,
                        alarmId
                    )
                )
                .build()
        }
    }
}