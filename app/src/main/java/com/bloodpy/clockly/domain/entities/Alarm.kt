package com.bloodpy.clockly.domain.entities

data class Alarm (
    val id: Int,
    val alarmTime: Long,
    val isActive: Boolean
)
