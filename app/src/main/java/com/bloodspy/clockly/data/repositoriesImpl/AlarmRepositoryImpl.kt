package com.bloodspy.clockly.data.repositoriesImpl

import com.bloodspy.clockly.data.database.AlarmDao
import com.bloodspy.clockly.data.mappers.AlarmMapper
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.domain.repositories.AlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val alarmDao: AlarmDao,
    private val mapper: AlarmMapper,
) : AlarmRepository {
    override fun getAlarmsFlow(): Flow<List<AlarmEntity>> {
        return alarmDao.getAlarmsFlow().map { mapper.mapListModelsToListEntities(it) }
    }

    override suspend fun getActivatedAlarms(): List<AlarmEntity>? {
        return alarmDao.getActivatedAlarms()?.map { mapper.mapModelToEntity(it) }
    }

    override suspend fun getAlarm(alarmId: Int): AlarmEntity? {
        return alarmDao.getAlarm(alarmId)?.let {
            mapper.mapModelToEntity(it)
        }
    }

    override suspend fun addAlarm(alarm: AlarmEntity): Long {
        return alarmDao.addAlarm(mapper.mapEntityToModel(alarm))
    }

    override suspend fun editAlarm(alarm: AlarmEntity) {
        alarmDao.addAlarm(mapper.mapEntityToModel(alarm))
    }

    override suspend fun deleteAlarm(alarmId: Int) {
        alarmDao.deleteAlarm(alarmId)
    }
}