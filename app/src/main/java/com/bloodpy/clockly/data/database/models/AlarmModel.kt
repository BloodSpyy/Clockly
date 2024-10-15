package com.bloodpy.clockly.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val alarmTime: Long,
    val isActive: Boolean
)

