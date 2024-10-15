package com.bloodpy.clockly.domain.entities

data class AlarmEntity (
    val id: Int,
    val alarmTime: Long,
    val isActive: Boolean
)
