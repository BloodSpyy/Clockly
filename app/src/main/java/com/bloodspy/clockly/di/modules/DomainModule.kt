package com.bloodspy.clockly.di.modules

import com.bloodspy.clockly.data.AlarmRepositoryImpl
import com.bloodspy.clockly.di.scopes.ApplicationScope
import com.bloodspy.clockly.domain.repository.AlarmRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {
    @Binds
    @ApplicationScope
    fun bindAlarmRepository(alarmRepositoryImpl: AlarmRepositoryImpl): AlarmRepository
}