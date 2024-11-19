package com.bloodspy.clockly.presentation.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bloodspy.clockly.AppApplication
import com.bloodspy.clockly.R
import com.bloodspy.clockly.databinding.FragmentAlarmBinding
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.enums.ScreenMode
import com.bloodspy.clockly.factories.ViewModelFactory
import com.bloodspy.clockly.helpers.TimeHelper
import com.bloodspy.clockly.presentation.states.AlarmStates
import com.bloodspy.clockly.presentation.viewmodels.AlarmViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class AlarmFragment : Fragment() {
    private lateinit var onEndWorkListener: OnEndWorkListener

    private lateinit var screenMode: ScreenMode

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AlarmViewModel::class.java]
    }

    private var _binding: FragmentAlarmBinding? = null
    private val binding: FragmentAlarmBinding
        get() = _binding ?: throw RuntimeException("FragmentAlarmBinding is null")

    private var alarmId = AlarmEntity.UNDEFINED_ID

    override fun onAttach(context: Context) {
        injectDependency()

        checkImplementListener(context)

        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAlarmBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeViewModel()
        setupAlarmTitle()
        setup24HourView()
        setupClickListeners()
        setupTimeChangedListener()
        setupChipGroupDaysOfWeekListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun subscribeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                with(binding) {
                    viewModel.state.collect {
                        progressBarLoading.visibility = View.GONE
                        textViewSave.isEnabled = true
                        timePickerAlarm.isEnabled = true

                        when (it) {
                            AlarmStates.Initial -> {
                                viewModel.getAlarm(alarmId)
                            }

                            AlarmStates.Loading -> {
                                progressBarLoading.visibility = View.VISIBLE
                                textViewSave.isEnabled = false
                                timePickerAlarm.isEnabled = false
                            }

                            is AlarmStates.DataLoaded -> {
                                setupTimePicker(it.alarmTimeParts)
                                setupTimeToAlarm(it.timeToAlarmParts)
                                setupRepetitionDays()
                            }

                            is AlarmStates.Success -> {
                                showSuccessToast(
                                    getCompletedStringTimeToStartAlarm(it.timeToAlarm)
                                )

                                onEndWorkListener.onEndWork()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupAlarmTitle() {
        binding.textViewAlarmTitle.text = when (screenMode) {
            ScreenMode.ADD_MODE -> getString(R.string.add_alarm_title)
            ScreenMode.EDIT_MODE -> getString(R.string.edit_alarm_title)
        }
    }

    private fun setup24HourView() {
        binding.timePickerAlarm.setIs24HourView(true)
    }

    private fun setupClickListeners() {
        setupSaveClickListener()
        setupCancelClickListener()
        setupChipRepeatingAlarmClickListener()
    }

    private fun setupChipGroupDaysOfWeekListener() {
        with(binding) {
            chipGroupDaysOfWeek.setOnCheckedStateChangeListener { _, checkedIds ->
                viewModel.updateTimeToStart(
                    getDaysOfWeekFromCheckedChipIds(checkedIds),
                    timePickerAlarm.hour,
                    timePickerAlarm.minute
                )
            }
        }
    }

    private fun setupChipRepeatingAlarmClickListener() {
        with(binding) {
            chipRepeatingAlarm.setOnClickListener {
                linearLayoutDaysOfWeek.visibility = if (chipRepeatingAlarm.isChecked) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }

    private fun setupSaveClickListener() {
        with(binding) {
            textViewSave.setOnClickListener {
                val hour = timePickerAlarm.hour
                val minute = timePickerAlarm.minute

                when (screenMode) {
                    ScreenMode.ADD_MODE -> viewModel.addAlarm(hour, minute)
                    ScreenMode.EDIT_MODE -> viewModel.editAlarm(alarmId, hour, minute)
                }
            }
        }
    }

    private fun setupCancelClickListener() {
        binding.textViewCancel.setOnClickListener {
            onEndWorkListener.onEndWork()
        }
    }

    private fun setupTimeChangedListener() {
        with(binding) {
            timePickerAlarm.setOnTimeChangedListener { _, hour, minute ->
                viewModel.updateTimeToStart(
                    getDaysOfWeekFromCheckedChipIds(chipGroupDaysOfWeek.checkedChipIds),
                    hour,
                    minute
                )
            }
        }

    }

    private fun getDaysOfWeekFromCheckedChipIds(chipIds: List<Int>): List<Int> {
        return chipIds.map {
            with(binding) {
                when (it) {
                    chipSunday.id -> Calendar.SUNDAY
                    chipMonday.id -> Calendar.MONDAY
                    chipTuesday.id -> Calendar.TUESDAY
                    chipWednesday.id -> Calendar.WEDNESDAY
                    chipThursday.id -> Calendar.THURSDAY
                    chipFriday.id -> Calendar.FRIDAY
                    chipSaturday.id -> Calendar.SATURDAY
                    else -> throw RuntimeException("Unkwown chip id")
                }
            }
        }
    }

    private fun setupTimePicker(alarmTimeParts: Array<Int>) {
        with(binding.timePickerAlarm) {
            hour = alarmTimeParts[1]
            minute = alarmTimeParts[2]
        }
    }

    private fun setupTimeToAlarm(timeToAlarmParts: Array<Int>) {
        with(binding.textViewTimeToAlarm) {
            text = getCompletedStringTimeToStartAlarm(
                timeToAlarmParts
            )
            visibility = View.VISIBLE
        }
    }

    private fun setupRepetitionDays() {
        with(binding) {
            textViewRepetitionDays.text = if (chipGroupDaysOfWeek.checkedChipIds.isEmpty()) {
                getString(R.string.repetition_days_one_time_alarm)
            } else {
                val repetitionDays = mutableListOf<String>()

                chipGroupDaysOfWeek.checkedChipIds.forEach {
                    repetitionDays.add(
                        when (it) {
                            chipSunday.id -> getString(R.string.repetition_days_sunday)
                            chipMonday.id -> getString(R.string.repetition_days_monday)
                            chipTuesday.id -> getString(R.string.repetition_days_tuesday)
                            chipWednesday.id -> getString(R.string.repetition_days_wednesday)
                            chipThursday.id -> getString(R.string.repetition_days_thursday)
                            chipFriday.id -> getString(R.string.repetition_days_friday)
                            chipSaturday.id -> getString(R.string.repetition_days_saturday)
                            else -> throw RuntimeException("Unkwown chip id")
                        }
                    )
                }

                if (repetitionDays.size == TimeHelper.DAYS_IN_WEEK) {
                    getString(R.string.repetition_days_every_day_alarm)
                } else {
                    repetitionDays.joinToString()
                }
            }
        }
    }

    private fun getCompletedStringTimeToStartAlarm(timeToAlarm: Array<Int>): String {
        return String.format(
            Locale.getDefault(),
            getString(R.string.time_to_alarm),
            TimeHelper.getFormattedTimeToStart(
                arrayOf(
                    resources.getStringArray(R.array.day_declination),
                    resources.getStringArray(R.array.hour_declination),
                    resources.getStringArray(R.array.minute_declination)
                ),
                timeToAlarm
            )
        )
    }

    private fun showSuccessToast(timeToAlarm: String) {
        Toast.makeText(
            requireContext(),
            timeToAlarm,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun parseParams() {
        with(requireArguments()) {
            if (!containsKey(SCREEN_MODE)) {
                throw RuntimeException("Param screen mode not found")
            }

            val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getParcelable(SCREEN_MODE, ScreenMode::class.java)
            } else {
                getParcelable(SCREEN_MODE)
            }

            if (mode != ScreenMode.ADD_MODE && mode != ScreenMode.EDIT_MODE) {
                throw RuntimeException("Unknown screen mode: $mode")
            }

            screenMode = mode

            if (screenMode == ScreenMode.EDIT_MODE) {
                if (!containsKey(ALARM_ID)) {
                    throw RuntimeException("Param alarm id not found")
                }

                alarmId = getInt(ALARM_ID, AlarmEntity.UNDEFINED_ID)
            }
        }
    }

    private fun checkImplementListener(context: Context) {
        if (context is OnEndWorkListener) {
            onEndWorkListener = context
        } else {
            throw RuntimeException("Activity must implement OnEndWorkListener")
        }
    }

    private fun injectDependency() {
        (requireActivity().application as AppApplication).component
            .inject(this)
    }

    interface OnEndWorkListener {
        fun onEndWork()
    }

    companion object {
        const val BACKSTACK_NAME = "alarm_fragment"

        private const val SCREEN_MODE = "screen_mode"
        private const val ALARM_ID = "alarm_id"

        fun newInstanceAddMode(): AlarmFragment = AlarmFragment().apply {
            arguments = Bundle().apply {
                putParcelable(SCREEN_MODE, ScreenMode.ADD_MODE)
            }
        }

        fun newInstanceEditMode(alarmId: Int): AlarmFragment {
            return AlarmFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(SCREEN_MODE, ScreenMode.EDIT_MODE)
                    putInt(ALARM_ID, alarmId)
                }
            }
        }
    }
}