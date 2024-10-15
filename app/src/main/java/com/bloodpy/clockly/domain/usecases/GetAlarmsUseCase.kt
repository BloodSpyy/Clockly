package com.bloodpy.clockly.domain.usecases

import com.bloodpy.clockly.domain.entities.AlarmEntity
import com.bloodpy.clockly.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlarmsUseCase @Inject constructor(private val alarmRepository: AlarmRepository) {
    suspend operator fun invoke(): Flow<List<AlarmEntity>> {
        return alarmRepository.getAlarms()
    }
}