package com.bloodspy.clockly.domain.repository

import com.bloodspy.clockly.domain.entities.AlarmEntity
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    suspend fun getAlarms(): Flow<List<AlarmEntity>>

    suspend fun getAlarm(alarmId: Int): AlarmEntity

    suspend fun addAlarm(alarm: AlarmEntity)

    suspend fun editAlarm(alarm: AlarmEntity)

    suspend fun deleteAlarm(alarmId: Int)
}