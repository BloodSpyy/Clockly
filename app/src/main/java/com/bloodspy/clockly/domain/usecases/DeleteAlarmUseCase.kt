package com.bloodspy.clockly.domain.usecases

import com.bloodspy.clockly.domain.repository.AlarmRepository
import javax.inject.Inject

class DeleteAlarmUseCase @Inject constructor(private val alarmRepository: AlarmRepository) {
    suspend operator fun invoke(alarmId: Int) {
        alarmRepository.deleteAlarm(alarmId)
    }
}