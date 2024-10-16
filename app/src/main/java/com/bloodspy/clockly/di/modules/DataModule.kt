package com.bloodspy.clockly.di.modules

import android.app.Application
import com.bloodspy.clockly.data.database.AlarmDao
import com.bloodspy.clockly.data.database.ClocklyDatabase
import com.bloodspy.clockly.di.scopes.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    companion object {
        @Provides
        @ApplicationScope
        fun provideAlarmDao(application: Application): AlarmDao {
            return ClocklyDatabase.getInstance(application).alarmDao()
        }
    }
}