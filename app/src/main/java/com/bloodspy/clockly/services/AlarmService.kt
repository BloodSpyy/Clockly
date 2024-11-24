package com.bloodspy.clockly.services

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import com.bloodspy.clockly.AppApplication
import com.bloodspy.clockly.R
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.domain.usecases.EditAlarmUseCase
import com.bloodspy.clockly.domain.usecases.GetAlarmUseCase
import com.bloodspy.clockly.domain.usecases.ScheduleAlarmUseCase
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
    lateinit var getAlarmUseCase: GetAlarmUseCase

    @Inject
    lateinit var editAlarmUseCase: EditAlarmUseCase

    @Inject
    lateinit var scheduleAlarmUseCase: ScheduleAlarmUseCase

    private lateinit var action: String

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val ringtoneMediaPlayer by lazy {
        RingtoneMediaPlayerHelper.createRingtoneMediaPlayer(this)
    }

    private val vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
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
        scope.cancel()

        super.onDestroy()
    }

    private fun chooseAction() {
        when (action) {
            START_ACTION -> {
                startAlarm()
            }

            STOP_ACTION -> {
                stopAlarm()
            }
        }
    }

    private fun startAlarm() {
        startForeground(
            ALARM_NOTIFICATION_ID,
            createNotification()
        )

        ringtoneMediaPlayer.start()
        startVibration()
    }

    private fun stopAlarm() {
        ringtoneMediaPlayer.stop()
        vibrator.cancel()

        val updateAlarmJob = scope.launch { updateAlarm() }

        updateAlarmJob.invokeOnCompletion { stopSelf() }
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
                getString(R.string.button_alarm_notification),
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

    private suspend fun updateAlarm() {
        val alarm = getAlarmUseCase(alarmId)

        alarm?.let {
            val changedAlarm = if (alarm.daysOfWeek != null) {
                handleRepeatingAlarm(alarm)
            } else {
                alarm.copy(isActive = false)
            }

            editAlarmUseCase(changedAlarm)
        }
    }

    private fun handleRepeatingAlarm(alarm: AlarmEntity): AlarmEntity {
        alarm.daysOfWeek?.let { daysOfWeek ->
            val newAlarmTime = getValidatedNearestRepeatAlarmTime(
                daysOfWeek, alarm.alarmTime
            )

            scheduleAlarmUseCase(alarm.id, newAlarmTime)

            return alarm.copy(alarmTime = newAlarmTime)
        } ?: throw RuntimeException("This method should be used only on repeating alarm")
    }

    private fun getValidatedNearestRepeatAlarmTime(
        daysOfWeek: String,
        timeInMillis: Long,
    ): Long {
        val calendar = java.util.Calendar.getInstance()

        val daysOfWeekList = daysOfWeek.split(",").map { it.toInt() }

        val validatedNearestRepeatAlarmTime = daysOfWeekList.minOf { day ->
            TimeHelper.validateRepeatAlarm(
                calendar.apply {
                    this.timeInMillis = timeInMillis
                    set(java.util.Calendar.DAY_OF_WEEK, day)
                }.timeInMillis
            )
        }

        return validatedNearestRepeatAlarmTime
    }

    private fun startVibration() {
        val vibrationPattern = longArrayOf(VIBRATION_DELAY, VIBRATION_TIME, VIBRATION_SLEEP)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createWaveform(vibrationPattern, REPEAT_INDEFINITELY)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(vibrationPattern, REPEAT_INDEFINITELY)
        }
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
        private const val VIBRATION_DELAY = 0L
        private const val VIBRATION_TIME = 1000L
        private const val VIBRATION_SLEEP = 700L

        private const val REPEAT_INDEFINITELY = 0

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