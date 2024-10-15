package com.bloodpy.clockly.data

import com.bloodpy.clockly.data.database.AlarmDao
import com.bloodpy.clockly.data.mappers.AlarmMapper
import com.bloodpy.clockly.domain.entities.AlarmEntity
import com.bloodpy.clockly.domain.repository.ClocklyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val alarmDao: AlarmDao,
    private val mapper: AlarmMapper
): ClocklyRepository {
    override suspend fun getAlarms(): Flow<List<AlarmEntity>> {
        return alarmDao.getAlarms().map { mapper.mapListModelsToListEntities(it) }
    }

    override suspend fun getAlarm(alarmId: Int): AlarmEntity {
        return mapper.mapModelToEntity(alarmDao.getAlarm(alarmId))
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