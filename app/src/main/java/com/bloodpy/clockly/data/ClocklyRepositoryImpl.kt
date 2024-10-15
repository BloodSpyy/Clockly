package com.bloodpy.clockly.data

import com.bloodpy.clockly.domain.entities.AlarmEntity
import com.bloodpy.clockly.domain.repository.ClocklyRepository
import kotlinx.coroutines.flow.Flow

class ClocklyRepositoryImpl: ClocklyRepository {
    override suspend fun getAlarms(): Flow<List<AlarmEntity>> {
        TODO("Not yet implemented")
    }

    override fun getAlarm(alarmId: Int): AlarmEntity {
        TODO("Not yet implemented")
    }

    override suspend fun addAlarm(alarm: AlarmEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun editAlarm(alarm: AlarmEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlarm(alarmId: Int) {
        TODO("Not yet implemented")
    }
}