package com.bloodpy.clockly.domain.usecases

import com.bloodpy.clockly.domain.entities.AlarmEntity
import com.bloodpy.clockly.domain.repository.AlarmRepository
import javax.inject.Inject

class GetAlarmUseCase @Inject constructor(private val alarmRepository: AlarmRepository) {
    suspend operator fun invoke(alarmId: Int): AlarmEntity {
        return alarmRepository.getAlarm(alarmId)
    }
}