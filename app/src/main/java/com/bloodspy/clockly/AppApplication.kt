package com.bloodspy.clockly

import android.app.Application
import com.bloodspy.clockly.di.DaggerClocklyComponent
import com.bloodspy.clockly.helpers.NotificationChannelsHelper

class AppApplication : Application() {
    val component by lazy {
        DaggerClocklyComponent.factory()
            .create(this)
    }

    override fun onCreate() {
        super.onCreate()
        NotificationChannelsHelper.createNotificationChannels(this)
    }
}