package com.bloodspy.clockly.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.provider.AlarmClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.bloodspy.clockly.AppApplication
import com.bloodspy.clockly.databinding.FragmentAlarmBinding
import com.bloodspy.clockly.presentation.states.AlarmStates
import com.bloodspy.clockly.presentation.viewmodels.AlarmViewModel
import com.bloodspy.clockly.presentation.viewmodels.factory.ViewModelFactory
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

class AlarmFragment : Fragment(){
    private lateinit var onEndWorkListener: OnEndWorkListener

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

        setup24HourView()
        subscribeViewModel()

        with(binding) {
            textViewSave.setOnClickListener {
                val calendar = Calendar.getInstance()

                val hour = timePickerAlarm.hour
                val minute = timePickerAlarm.minute

                calendar.set(hour, Calendar.HOUR_OF_DAY)
                calendar.set(minute, Calendar.MINUTE)

                viewModel.addAlarm(hour, minute)
            }

            textViewCancel.setOnClickListener {
                onEndWorkListener.onEndWork()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        //todo подумай, оставить здесь или перенести в onViewCreated()
        if (screenMode == EDIT_MODE) {
            viewModel.getAlarm(alarmId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
    
    private fun setup24HourView() {
        binding.timePickerAlarm.setIs24HourView(true)
    }

    private fun subscribeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            with(binding) {
                viewModel.state.collect {
                    progressBarLoading.visibility = View.GONE
                    textViewSave.isEnabled = true
                    textViewCancel.isEnabled = true

                    when (it) {
                        is AlarmStates.DataLoaded -> {
                            //todo внеси сюда часы и минуты с alarmEntity
                        }

                        AlarmStates.Initial -> {
                            val calendar = Calendar.getInstance()

                            val hour = calendar.get(Calendar.HOUR_OF_DAY)
                            val minute = calendar.get(Calendar.MINUTE)

                            timePickerAlarm.hour = hour
                            timePickerAlarm.minute = minute
                        }

                        AlarmStates.Loading -> {
                            progressBarLoading.visibility = View.VISIBLE
                            textViewSave.isEnabled = false
                            textViewCancel.isEnabled = false
                        }

                        AlarmStates.Success -> TODO()
                    }
                }
            }
        }
    }

    private fun injectDependency() {
        (requireActivity().application as AppApplication).component
            .inject(this)
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

    fun checkImplementListener(context: Context) {
        if(context is OnEndWorkListener) {
            onEndWorkListener = context
        } else {
            throw RuntimeException("Activity must implement OnEndWorkListener")
        }
    }

    interface OnEndWorkListener {
        fun onEndWork()
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