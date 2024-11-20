package com.bloodspy.clockly.data.mappers

import com.bloodspy.clockly.data.database.models.AlarmModel
import com.bloodspy.clockly.domain.entities.AlarmEntity
import javax.inject.Inject

class AlarmMapper @Inject constructor() {
    fun mapEntityToModel(alarmEntity: AlarmEntity): AlarmModel = AlarmModel(
        id = alarmEntity.id,
        alarmTime = alarmEntity.alarmTime,
        isActive = alarmEntity.isActive,
        daysOfWeek = alarmEntity.daysOfWeek
    )

    fun mapModelToEntity(alarmModel: AlarmModel): AlarmEntity = AlarmEntity(
        id = alarmModel.id,
        alarmTime = alarmModel.alarmTime,
        isActive = alarmModel.isActive,
        daysOfWeek = alarmModel.daysOfWeek
    )

    fun mapListModelsToListEntities(models: List<AlarmModel>): List<AlarmEntity> {
        return models.map { mapModelToEntity(it) }
    }
}