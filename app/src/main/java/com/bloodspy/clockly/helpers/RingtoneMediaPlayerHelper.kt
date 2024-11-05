package com.bloodspy.clockly.helpers

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri

object RingtoneMediaPlayerHelper {
    fun createRingtoneMediaPlayer(context: Context): MediaPlayer = MediaPlayer.create(
        context,
        getRingtoneUri()
    ).apply { isLooping = true }

    private fun getRingtoneUri(): Uri {
        return RingtoneManager.getDefaultUri(
            RingtoneManager.TYPE_ALARM
        ) ?: RingtoneManager.getDefaultUri(
            RingtoneManager.TYPE_RINGTONE
        )
    }
}

