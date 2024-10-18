package com.bloodspy.clockly.di.modules

import com.bloodspy.clockly.data.AlarmRepositoryImpl
import com.bloodspy.clockly.data.AlarmSchedulerImpl
import com.bloodspy.clockly.di.scopes.ClocklyAppScope
import com.bloodspy.clockly.domain.repository.AlarmRepository
import com.bloodspy.clockly.domain.scheduler.AlarmScheduler
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {
    @Binds
    @ClocklyAppScope
    fun bindAlarmRepository(alarmRepositoryImpl: AlarmRepositoryImpl): AlarmRepository

    @Binds
    @ClocklyAppScope
    fun bindAlarmScheduler(alarmSchedulerImpl: AlarmSchedulerImpl): AlarmScheduler
}