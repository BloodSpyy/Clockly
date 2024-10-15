package com.bloodpy.clockly.di.modules

import com.bloodpy.clockly.data.AlarmRepositoryImpl
import com.bloodpy.clockly.di.scopes.ApplicationScope
import com.bloodpy.clockly.domain.repository.AlarmRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {
    @Binds
    @ApplicationScope
    fun bindAlarmRepository(alarmRepositoryImpl: AlarmRepositoryImpl): AlarmRepository
}