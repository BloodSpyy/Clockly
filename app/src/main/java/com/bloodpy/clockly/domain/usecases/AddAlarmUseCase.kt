package com.bloodpy.clockly.domain.usecases

import com.bloodpy.clockly.domain.entities.AlarmEntity
import com.bloodpy.clockly.domain.repository.ClocklyRepository

class AddAlarmUseCase(private val clocklyRepository: ClocklyRepository) {
    suspend operator fun invoke(alarm: AlarmEntity) {
        clocklyRepository.addAlarm(alarm)
    }
}