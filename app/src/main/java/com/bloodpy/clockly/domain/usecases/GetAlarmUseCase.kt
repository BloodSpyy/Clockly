package com.bloodpy.clockly.domain.usecases

import com.bloodpy.clockly.domain.entities.AlarmEntity
import com.bloodpy.clockly.domain.repository.ClocklyRepository
import kotlinx.coroutines.flow.Flow

class GetAlarmUseCase (private val clocklyRepository: ClocklyRepository) {
    operator fun invoke(alarmId: Int): AlarmEntity {
        return clocklyRepository.getAlarm(alarmId)
    }
}