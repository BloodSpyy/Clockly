package com.bloodpy.clockly.data.mappers

import com.bloodpy.clockly.data.database.models.AlarmModel
import com.bloodpy.clockly.domain.entities.AlarmEntity
import javax.inject.Inject

class AlarmMapper @Inject constructor() {
    fun mapEntityToModel(alarmEntity: AlarmEntity): AlarmModel = AlarmModel(
        id = alarmEntity.id,
        alarmTime = alarmEntity.alarmTime,
        isActive = alarmEntity.isActive
    )

    fun mapModelToEntity(alarmModel: AlarmModel): AlarmEntity = AlarmEntity(
        id = alarmModel.id,
        alarmTime = alarmModel.alarmTime,
        isActive = alarmModel.isActive
    )

    fun mapListModelsToListEntities(models: List<AlarmModel>): List<AlarmEntity> {
        return models.map { mapModelToEntity(it) }
    }
}