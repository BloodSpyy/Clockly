package com.bloodspy.clockly.domain.usecases

import com.bloodspy.clockly.domain.repositories.AlarmRepository
import javax.inject.Inject

class GetNearestAlarmTime @Inject constructor(
    private val alarmRepository: AlarmRepository,
) {
    suspend operator fun invoke(): Long? = alarmRepository.getNearestAlarmTime()
}