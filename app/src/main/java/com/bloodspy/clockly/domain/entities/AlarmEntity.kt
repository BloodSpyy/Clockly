package com.bloodspy.clockly.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data class AlarmEntity @Inject constructor(
    val id: Int,
    val alarmTime: Long,
    val isActive: Boolean
) : Parcelable
