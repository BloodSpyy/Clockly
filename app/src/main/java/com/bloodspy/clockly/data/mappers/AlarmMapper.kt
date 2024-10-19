package com.bloodspy.clockly.data.mappers

import com.bloodspy.clockly.data.database.models.AlarmModel
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.utils.parseTimeToLong
import com.bloodspy.clockly.utils.parseTimeToString
import javax.inject.Inject

class AlarmMapper @Inject constructor() {
    fun mapEntityToModel(alarmEntity: AlarmEntity): AlarmModel = AlarmModel(
        id = alarmEntity.id,
        alarmTime = parseTimeToLong(alarmEntity.alarmTime),
        isActive = alarmEntity.isActive
    )

    fun mapModelToEntity(alarmModel: AlarmModel): AlarmEntity = AlarmEntity(
        id = alarmModel.id,
        alarmTime = parseTimeToString(alarmModel.alarmTime),
        isActive = alarmModel.isActive
    )

    fun mapListModelsToListEntities(models: List<AlarmModel>): List<AlarmEntity> {
        return models.map { mapModelToEntity(it) }
    }
}