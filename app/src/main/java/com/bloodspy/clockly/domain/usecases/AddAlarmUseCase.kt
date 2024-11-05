package com.bloodspy.clockly.domain.usecases

import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.domain.repositories.AlarmRepository
import javax.inject.Inject

class AddAlarmUseCase @Inject constructor(private val alarmRepository: AlarmRepository) {
    suspend operator fun invoke(alarm: AlarmEntity): Long = alarmRepository.addAlarm(alarm)
}