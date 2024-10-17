package com.bloodspy.clockly.presentation.recyclerViewUtils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bloodspy.clockly.databinding.ItemAlarmBinding
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.presentation.recyclerViewUtils.callback.AlarmDiffCallback
import com.bloodspy.clockly.presentation.recyclerViewUtils.viewholder.AlarmViewHolder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AlarmAdapter : ListAdapter<AlarmEntity, AlarmViewHolder>(
    AlarmDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val binding = ItemAlarmBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AlarmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = getItem(position)

        with(holder.binding) {
            textViewAlarmTime.text = parseTime(alarm.alarmTime)
            switchAlarm.isChecked = alarm.isActive
        }
    }

    private fun parseTime(timeInMillis: Long): String {
        val timeInDate = Date(timeInMillis)

        val simpleDataFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        return simpleDataFormat.format(timeInDate)
    }
}