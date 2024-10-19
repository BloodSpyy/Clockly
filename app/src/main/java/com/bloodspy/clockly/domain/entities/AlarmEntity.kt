package com.bloodspy.clockly.domain.entities

import javax.inject.Inject

data class AlarmEntity @Inject constructor(
    val id: Int,
    val alarmTime: String,
    val isActive: Boolean
)
