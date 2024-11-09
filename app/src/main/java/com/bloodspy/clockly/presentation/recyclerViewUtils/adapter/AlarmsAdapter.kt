package com.bloodspy.clockly.presentation.recyclerViewUtils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bloodspy.clockly.databinding.ItemAlarmBinding
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.helpers.AlarmTimeHelper
import com.bloodspy.clockly.presentation.recyclerViewUtils.callback.AlarmsDiffCallback
import com.bloodspy.clockly.presentation.recyclerViewUtils.viewholder.AlarmsViewHolder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AlarmsAdapter : ListAdapter<AlarmEntity, AlarmsViewHolder>(
    AlarmsDiffCallback()
) {
    var onChangedEnableStateListener: ((AlarmEntity) -> Unit)? = null
    var onAlarmClickListener: ((AlarmEntity) -> Unit)? = null

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
                onChangedEnableStateListener?.invoke(alarm) ?: throw RuntimeException(
                    "onChangedEnableStateListener is null"
                )
            }

            textViewAlarmTime.setOnClickListener {
                onAlarmClickListener?.invoke(alarm) ?: throw RuntimeException(
                    "onAlarmClickListener is null"
                )
            }

            //todo подумай как убрать здесь преобразования и перенести их отсюда
            textViewAlarmTime.text = AlarmTimeHelper.parseAlarmTime(alarm.alarmTime)
            switchAlarm.isChecked = alarm.isActive
        }
    }
}