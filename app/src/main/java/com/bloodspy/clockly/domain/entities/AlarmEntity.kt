package com.bloodspy.clockly.domain.entities

import javax.inject.Inject

data class AlarmEntity @Inject constructor(
    //todo подумай, тут нужен val или var
    val id: Int = UNDEFINED_ID,
    val alarmTime: Long,
    val isActive: Boolean = true,
) {
    companion object {
        private const val UNDEFINED_ID = 0
    }
}
