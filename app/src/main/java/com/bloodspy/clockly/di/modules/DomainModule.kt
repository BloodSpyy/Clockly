package com.bloodspy.clockly.di.modules

import com.bloodspy.clockly.data.AlarmRepositoryImpl
import com.bloodspy.clockly.di.scopes.ClocklyAppScope
import com.bloodspy.clockly.domain.repository.AlarmRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {
    @Binds
    @ClocklyAppScope
    fun bindAlarmRepository(alarmRepositoryImpl: AlarmRepositoryImpl): AlarmRepository
}