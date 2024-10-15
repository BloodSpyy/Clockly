package com.bloodpy.clockly.domain.usecases

import com.bloodpy.clockly.domain.repository.ClocklyRepository

class DeleteAlarmUseCase (private val clocklyRepository: ClocklyRepository) {
    suspend operator fun invoke(alarmId: Int) {
        clocklyRepository.deleteAlarm(alarmId)
    }
}