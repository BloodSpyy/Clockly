package com.bloodspy.clockly.presentation.recyclerViewUtils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bloodspy.clockly.databinding.ItemAlarmBinding
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.presentation.recyclerViewUtils.callback.AlarmsDiffCallback
import com.bloodspy.clockly.presentation.recyclerViewUtils.viewholder.AlarmsViewHolder
import com.bloodspy.clockly.utils.parseTime

class AlarmsAdapter : ListAdapter<AlarmEntity, AlarmsViewHolder>(
    AlarmsDiffCallback()
) {
    var onChangedEnableState: ((AlarmEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmsViewHolder {
        val binding = ItemAlarmBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AlarmsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmsViewHolder, position: Int) {
        val alarm = getItem(position)

        with(holder.binding) {
            switchAlarm.setOnClickListener {
                onChangedEnableState?.invoke(alarm) ?: throw RuntimeException(
                    "onChangedEnableState is null"
                )
            }

            textViewAlarmTime.text = parseTime(alarm.alarmTime)
            switchAlarm.isChecked = alarm.isActive
        }
    }
}