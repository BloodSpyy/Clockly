package com.bloodspy.clockly.services

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.bloodspy.clockly.AppApplication
import com.bloodspy.clockly.R
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.domain.repositories.AlarmRepository
import com.bloodspy.clockly.helpers.AlarmServiceHelper
import com.bloodspy.clockly.helpers.NotificationChannelsHelper.ALARM_NOTIFICATION_CHANNEL_ID
import com.bloodspy.clockly.helpers.RingtoneMediaPlayerHelper
import com.bloodspy.clockly.helpers.TimeHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlarmService : Service() {
    @Inject
    lateinit var alarmRepository: AlarmRepository

    private lateinit var action: String

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val ringtoneMediaPlayer by lazy {
        RingtoneMediaPlayerHelper.createRingtoneMediaPlayer(this)
    }

    private var alarmId = AlarmEntity.UNDEFINED_ID

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        injectDependency()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        parseIntent(intent)

        chooseAction()

        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        stopAlarm()

        super.onDestroy()
    }

    private fun chooseAction() {
        when (action) {
            START_ACTION -> {
                startAlarm()
            }

            STOP_ACTION -> {
                stopSelf()
            }
        }
    }

    private fun startAlarm() {
        startForeground(
            ALARM_NOTIFICATION_ID,
            createNotification()
        )

        ringtoneMediaPlayer.start()

        setInactiveAlarmStatus()
    }

    private fun stopAlarm() {
        ringtoneMediaPlayer.stop()

        scope.cancel()
    }

    private fun setInactiveAlarmStatus() {
        scope.launch {
            val alarm = alarmRepository.getAlarm(alarmId)

            alarm?.let {
                alarmRepository.editAlarm(it.copy(isActive = false))
            }
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(
            this,
            ALARM_NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(
                String.format(
                    getString(R.string.alarm_notification),
                    TimeHelper.getParsedTime(Calendar.getInstance().timeInMillis)
                )
            )
            .addAction(
                R.mipmap.ic_launcher_round,
                getString(R.string.alarm_notification_button),
                AlarmServiceHelper.getStopAlarmServicePendingIntent(
                    this,
                    alarmId
                )
            )
            .setDeleteIntent(
                AlarmServiceHelper.getStopAlarmServicePendingIntent(
                    this,
                    alarmId
                )
            )
            .build()
    }

    private fun injectDependency() {
        (this.application as AppApplication).component
            .inject(this)
    }

    private fun parseIntent(intent: Intent?) {
        if (intent == null) {
            throw RuntimeException("AlarmService intent is null")
        }

        if (!intent.hasExtra(EXTRA_ACTION)) {
            throw RuntimeException("Param action not found")
        }

        val intentAction = intent.getStringExtra(EXTRA_ACTION).toString()

        if (intentAction != START_ACTION && intentAction != STOP_ACTION) {
            throw RuntimeException("Unknown action: $intentAction")
        }

        action = intentAction

        if (!intent.hasExtra(EXTRA_ALARM_ID)) {
            throw RuntimeException("Param alarm id not found")
        }

        alarmId = intent.getIntExtra(EXTRA_ALARM_ID, AlarmEntity.UNDEFINED_ID)
    }

    companion object {
        private const val ALARM_NOTIFICATION_ID = 1

        private const val EXTRA_ACTION = "action"
        private const val EXTRA_ALARM_ID = "alarm_id"

        private const val START_ACTION = "start"
        private const val STOP_ACTION = "stop"

        fun newIntentStartAlarm(context: Context, alarmId: Int): Intent = Intent(
            context, AlarmService::class.java
        ).apply {
            putExtra(EXTRA_ACTION, START_ACTION)
            putExtra(EXTRA_ALARM_ID, alarmId)
        }

        fun newIntentStopAlarm(context: Context, alarmId: Int): Intent = Intent(
            context, AlarmService::class.java
        ).apply {
            putExtra(EXTRA_ACTION, STOP_ACTION)
            putExtra(EXTRA_ALARM_ID, alarmId)
        }
    }
}