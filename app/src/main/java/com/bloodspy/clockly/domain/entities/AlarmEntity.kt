package com.bloodspy.clockly.domain.entities

import javax.inject.Inject

data class AlarmEntity @Inject constructor(
    var id: Int? = null,
    val alarmTime: String,
    val isActive: Boolean,
)
