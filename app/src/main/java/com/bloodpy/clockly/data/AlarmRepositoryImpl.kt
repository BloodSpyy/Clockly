package com.bloodpy.clockly.data

import com.bloodpy.clockly.data.database.AlarmDao
import com.bloodpy.clockly.data.mappers.AlarmMapper
import com.bloodpy.clockly.domain.entities.AlarmEntity
import com.bloodpy.clockly.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val alarmDao: AlarmDao,
    private val mapper: AlarmMapper
): AlarmRepository {
    override suspend fun getAlarms(): Flow<List<AlarmEntity>> {
        return alarmDao.getAlarms().map { mapper.mapListModelsToListEntities(it) }
    }

    override suspend fun getAlarm(alarmId: Int): AlarmEntity {
        return mapper.mapModelToEntity(alarmDao.getAlarm(alarmId))
    }

    override suspend fun addAlarm(alarm: AlarmEntity) {
        alarmDao.addAlarm(mapper.mapEntityToModel(alarm))
    }

    override suspend fun editAlarm(alarm: AlarmEntity) {
        alarmDao.addAlarm(mapper.mapEntityToModel(alarm))
    }

    override suspend fun deleteAlarm(alarmId: Int) {
        alarmDao.deleteAlarm(alarmId)
    }
}