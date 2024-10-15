package com.bloodpy.clockly.domain.usecases

import com.bloodpy.clockly.domain.entities.AlarmEntity
import com.bloodpy.clockly.domain.repository.ClocklyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlarmsUseCase @Inject constructor(private val clocklyRepository: ClocklyRepository) {
    suspend operator fun invoke(): Flow<List<AlarmEntity>> {
        return clocklyRepository.getAlarms()
    }
}