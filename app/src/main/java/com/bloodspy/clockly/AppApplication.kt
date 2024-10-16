package com.bloodspy.clockly

import android.app.Application
import com.bloodspy.clockly.di.DaggerClocklyComponent

class AppApplication: Application() {
    val component by lazy {
        DaggerClocklyComponent.factory()
            .create(this)
    }
}