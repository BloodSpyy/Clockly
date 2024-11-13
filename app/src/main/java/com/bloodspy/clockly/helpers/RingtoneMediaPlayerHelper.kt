package com.bloodspy.clockly.helpers

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri

object RingtoneMediaPlayerHelper {
    fun createRingtoneMediaPlayer(context: Context): MediaPlayer {
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .build()

        return MediaPlayer().apply {
            setAudioAttributes(audioAttributes)
            setDataSource(context, getRingtoneUri())
            isLooping = true
            prepare()
        }
    }

    private fun getRingtoneUri(): Uri {
        return RingtoneManager.getDefaultUri(
            RingtoneManager.TYPE_ALARM
        ) ?: RingtoneManager.getDefaultUri(
            RingtoneManager.TYPE_RINGTONE
        )
    }
}

