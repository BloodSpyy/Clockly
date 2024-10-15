package com.bloodpy.clockly.di.modules

import android.app.Application
import com.bloodpy.clockly.data.database.AlarmDao
import com.bloodpy.clockly.data.database.ClocklyDatabase
import com.bloodpy.clockly.di.scopes.ApplicationScope
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