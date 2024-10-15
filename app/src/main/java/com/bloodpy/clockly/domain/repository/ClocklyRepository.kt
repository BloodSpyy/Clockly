package com.bloodpy.clockly.domain.repository

import com.bloodpy.clockly.domain.entities.AlarmEntity
import kotlinx.coroutines.flow.Flow

interface ClocklyRepository {
    suspend fun getAlarms(): Flow<List<AlarmEntity>>

    fun getAlarm(alarmId: Int): AlarmEntity

    suspend fun addAlarm(alarm: AlarmEntity)

    suspend fun editAlarm(alarm: AlarmEntity)

    suspend fun deleteAlarm(alarmId: Int)
}