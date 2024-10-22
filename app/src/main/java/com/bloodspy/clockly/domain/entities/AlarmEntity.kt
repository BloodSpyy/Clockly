package com.bloodspy.clockly.domain.entities

import javax.inject.Inject

data class AlarmEntity @Inject constructor(
    val id: Int = UNDEFINED_ID,
    val alarmTime: Long,
    val isActive: Boolean = true,
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}
