package com.bloodpy.clockly.domain.usecases

import com.bloodpy.clockly.domain.entities.AlarmEntity
import com.bloodpy.clockly.domain.repository.AlarmRepository
import javax.inject.Inject

//todo не забудь сделать bind с репозитория в реализацию во всех usecase
class AddAlarmUseCase @Inject constructor(private val alarmRepository: AlarmRepository) {
    suspend operator fun invoke(alarm: AlarmEntity) {
        alarmRepository.addAlarm(alarm)
    }
}