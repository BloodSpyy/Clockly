package com.bloodspy.clockly.presentation.recyclerViewUtils.callback

import androidx.recyclerview.widget.DiffUtil
import com.bloodspy.clockly.domain.entities.AlarmEntity

class AlarmsDiffCallback : DiffUtil.ItemCallback<AlarmEntity>() {
    override fun areItemsTheSame(oldItem: AlarmEntity, newItem: AlarmEntity): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: AlarmEntity, newItem: AlarmEntity): Boolean =
        oldItem == newItem
}