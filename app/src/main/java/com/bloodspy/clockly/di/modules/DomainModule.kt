package com.bloodspy.clockly.di.modules

import com.bloodspy.clockly.data.repositoriesImpl.AlarmRepositoryImpl
import com.bloodspy.clockly.data.repositoriesImpl.AlarmSchedulerRepositoryImpl
import com.bloodspy.clockly.di.scopes.ClocklyAppScope
import com.bloodspy.clockly.domain.repositories.AlarmRepository
import com.bloodspy.clockly.domain.repositories.AlarmSchedulerRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {
    @Binds
    @ClocklyAppScope
    fun bindAlarmRepository(alarmRepositoryImpl: AlarmRepositoryImpl): AlarmRepository

    @Binds
    @ClocklyAppScope
    fun bindAlarmSchedulerRepository(
        alarmSchedulerRepositoryImpl: AlarmSchedulerRepositoryImpl,
    ): AlarmSchedulerRepository
}