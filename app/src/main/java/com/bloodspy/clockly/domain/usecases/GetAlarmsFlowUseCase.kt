package com.bloodspy.clockly.domain.usecases

import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.domain.repositories.AlarmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlarmsFlowUseCase @Inject constructor(private val alarmRepository: AlarmRepository) {
    operator fun invoke(): Flow<List<AlarmEntity>> {
        return alarmRepository.getAlarmsFlow()
    }
}