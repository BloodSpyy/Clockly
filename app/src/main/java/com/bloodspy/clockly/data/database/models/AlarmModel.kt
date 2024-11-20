package com.bloodspy.clockly.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val alarmTime: Long,
    val isActive: Boolean,
    val daysOfWeek: String?,
)