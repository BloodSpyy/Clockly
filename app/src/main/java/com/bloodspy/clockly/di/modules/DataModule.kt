package com.bloodspy.clockly.di.modules

import android.app.Application
import com.bloodspy.clockly.data.database.AlarmDao
import com.bloodspy.clockly.data.database.ClocklyDatabase
import com.bloodspy.clockly.di.scopes.ClocklyAppScope
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    companion object {
        @Provides
        @ClocklyAppScope
        fun provideAlarmDao(application: Application): AlarmDao {
            return ClocklyDatabase.getInstance(application).alarmDao()
        }
    }
}