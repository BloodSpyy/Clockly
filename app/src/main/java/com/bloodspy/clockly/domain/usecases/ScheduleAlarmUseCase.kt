package com.bloodspy.clockly.domain.usecases

import com.bloodspy.clockly.domain.repositories.AlarmSchedulerRepository
import javax.inject.Inject

class ScheduleAlarmUseCase @Inject constructor(
    private val alarmSchedulerRepository: AlarmSchedulerRepository,
) {
    operator fun invoke(alarmId: Int, timeInMillis: Long) {
        alarmSchedulerRepository.schedule(alarmId, timeInMillis)
    }
}