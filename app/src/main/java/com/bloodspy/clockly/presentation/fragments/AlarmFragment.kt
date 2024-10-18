package com.bloodspy.clockly.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bloodspy.clockly.databinding.FragmentAlarmBinding

class AlarmFragment : Fragment() {
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

        when(screenMode) {
            ADD_MODE -> {
                launchAddMode()
            }
            EDIT_MODE -> {
                launchEditMode()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()


        _binding = null
    }

    private fun launchAddMode() {
        binding.buttonSave.setOnClickListener {

        }

        binding.buttonCancel.setOnClickListener {

        }
    }

    private fun launchEditMode() {

    }

    private fun loadInitialValue() {

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