package com.bloodspy.clockly.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bloodspy.clockly.databinding.FragmentAlarmBinding
import com.bloodspy.clockly.presentation.viewmodels.AlarmViewModel
import com.bloodspy.clockly.presentation.viewmodels.factory.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlarmFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AlarmViewModel::class.java]
    }

    private var _binding: FragmentAlarmBinding? = null
    private val binding: FragmentAlarmBinding
        get() = _binding ?: throw RuntimeException("FragmentAlarmBinding is null")

    private var alarmId = UNDEFINED_ID
    private var screenMode = UNKNOWN_SCREEN_MODE

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

        set24HourView()
        chooseScreenMode()
    }

    override fun onDestroyView() {
        super.onDestroyView()


        _binding = null
    }

    private fun chooseScreenMode() {
        when(screenMode) {
            ADD_MODE -> {
                launchAddMode()
            }
            EDIT_MODE -> {
                launchEditMode()
            }
        }
    }

    private fun launchAddMode() {
        with(binding) {
            buttonSave.setOnClickListener {

            }

            buttonCancel.setOnClickListener {

            }
        }
    }

    private fun launchEditMode() {
        loadInitialValue()

        with(binding) {
            buttonSave.setOnClickListener {

            }

            buttonCancel.setOnClickListener {

            }
        }
    }

    private fun loadInitialValue() {
        viewLifecycleOwner.lifecycleScope.launch {
            val alarm = viewModel.alarm(alarmId)

            val alarmTime = alarm.alarmTime

            binding.timePickerAlarm.
        }
    }

    private fun set24HourView() {
        binding.timePickerAlarm.setIs24HourView(true)
    }

    private fun parseParams() {
        with(requireArguments()) {
            if (!containsKey(SCREEN_MODE)) {
                throw RuntimeException("Param screen mode not found")
            }

            val mode = getString(SCREEN_MODE).toString()

            if (mode != ADD_MODE && mode != EDIT_MODE) {
                throw RuntimeException("Unknown screen mode: $mode")
            }

            screenMode = mode

            if (screenMode == EDIT_MODE) {
                if (!containsKey(ALARM_ID)) {
                    throw RuntimeException("Param alarm id not found")
                }

                alarmId = getInt(ALARM_ID)
            }
        }
    }


    companion object {
        const val BACKSTACK_NAME = "alarm_fragment"

        private const val SCREEN_MODE = "screen_mode"
        private const val ALARM_ID = "alarm_id"

        private const val ADD_MODE = "add_mode"
        private const val EDIT_MODE = "edit_mode"

        private const val UNDEFINED_ID = 0
        private const val UNKNOWN_SCREEN_MODE = "unknown"

        fun newInstanceAddMode(): AlarmFragment = AlarmFragment().apply {
            arguments = Bundle().apply {
                putString(SCREEN_MODE, ADD_MODE)
            }
        }

        fun newInstanceEditMode(alarmId: Int): AlarmFragment {
            return AlarmFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, EDIT_MODE)
                    putInt(ALARM_ID, alarmId)
                }
            }
        }
    }
}