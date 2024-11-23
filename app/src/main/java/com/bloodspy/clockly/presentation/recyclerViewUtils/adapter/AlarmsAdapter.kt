package com.bloodspy.clockly.presentation.recyclerViewUtils.adapter

import android.content.Context
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.ListAdapter
import com.bloodspy.clockly.R
import com.bloodspy.clockly.databinding.ItemAlarmBinding
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.helpers.TimeHelper
import com.bloodspy.clockly.presentation.recyclerViewUtils.callback.AlarmsDiffCallback
import com.bloodspy.clockly.presentation.recyclerViewUtils.viewholder.AlarmsViewHolder

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

            textViewAlarmTime.text = TimeHelper.getParsedTime(alarm.alarmTime)
            switchAlarm.isChecked = alarm.isActive
            textViewRepetitionDays.text =
                getRepetitionDays(holder.itemView.context, alarm.daysOfWeek)
        }
    }

    private fun getRepetitionDays(context: Context, daysOfWeek: String?): String {
        return if (daysOfWeek == null) {
            getString(context, R.string.repetition_days_one_time_alarm)
        } else {
            val repetitionDays = getRepetitionDaysFromDaysOfWeek(context, daysOfWeek)

            if (repetitionDays.size == TimeHelper.DAYS_IN_WEEK) {
                getString(context, R.string.repetition_days_every_day_alarm)
            } else {
                repetitionDays.joinToString()
            }
        }
    }

    private fun getRepetitionDaysFromDaysOfWeek(
        context: Context,
        daysOfWeek: String,
    ): List<String> = daysOfWeek.split(",").map { dayOfWeek ->
        when (dayOfWeek.toInt()) {
            Calendar.SUNDAY -> getString(context, R.string.repetition_days_sunday)
            Calendar.MONDAY -> getString(context, R.string.repetition_days_monday)
            Calendar.TUESDAY -> getString(context, R.string.repetition_days_tuesday)
            Calendar.WEDNESDAY -> getString(context, R.string.repetition_days_wednesday)
            Calendar.THURSDAY -> getString(context, R.string.repetition_days_thursday)
            Calendar.FRIDAY -> getString(context, R.string.repetition_days_friday)
            Calendar.SATURDAY -> getString(context, R.string.repetition_days_saturday)
            else -> throw RuntimeException("Unknown day of week: $dayOfWeek")
        }
    }
}