package com.bloodspy.clockly.domain.usecases

import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.domain.repositories.AlarmRepository
import javax.inject.Inject

class GetAlarmUseCase @Inject constructor(private val alarmRepository: AlarmRepository) {
    suspend operator fun invoke(alarmId: Int): AlarmEntity? {
        return alarmRepository.getAlarm(alarmId)
    }
}