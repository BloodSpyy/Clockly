package com.bloodpy.clockly.domain.usecases

import com.bloodpy.clockly.domain.repository.ClocklyRepository
import javax.inject.Inject

class DeleteAlarmUseCase @Inject constructor(private val clocklyRepository: ClocklyRepository) {
    suspend operator fun invoke(alarmId: Int) {
        clocklyRepository.deleteAlarm(alarmId)
    }
}