package com.bloodspy.clockly.domain.repositories

import com.bloodspy.clockly.domain.entities.AlarmEntity
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    fun getAlarms(): Flow<List<AlarmEntity>>

    suspend fun getAlarm(alarmId: Int): AlarmEntity

    suspend fun getNearestAlarmTime(): Long

    suspend fun addAlarm(alarm: AlarmEntity): Long

    suspend fun editAlarm(alarm: AlarmEntity)

    suspend fun deleteAlarm(alarmId: Int)
}