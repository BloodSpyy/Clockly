package com.bloodspy.clockly.domain.usecases

import com.bloodspy.clockly.domain.repositories.AlarmRepository
import javax.inject.Inject

class GetNearestAlarmTimeUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
) {
    operator fun invoke() = alarmRepository.getNearestAlarmTime()
}