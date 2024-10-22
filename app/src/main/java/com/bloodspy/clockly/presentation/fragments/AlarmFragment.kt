package com.bloodspy.clockly.presentation.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.bloodspy.clockly.AppApplication
import com.bloodspy.clockly.databinding.FragmentAlarmBinding
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.enums.ScreenMode
import com.bloodspy.clockly.presentation.states.AlarmStates
import com.bloodspy.clockly.presentation.viewmodels.AlarmViewModel
import com.bloodspy.clockly.presentation.viewmodels.factory.ViewModelFactory
import kotlinx.coroutines.launch
import kotlinx.coroutines.processNextEventInCurrentThread
import java.util.Calendar
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

    private var alarmId = UNDEFINED_ID

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
        setupClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun setup24HourView() {
        binding.timePickerAlarm.setIs24HourView(true)
    }

    private fun setupClickListeners() {
        with(binding) {
            textViewSave.setOnClickListener {
                val hour = timePickerAlarm.hour
                val minute = timePickerAlarm.minute

                viewModel.addAlarm(hour, minute)
            }

            textViewCancel.setOnClickListener {
                onEndWorkListener.onEndWork()
            }
        }
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
                            is AlarmStates.Initial -> {
                                viewModel.getAlarm(
                                    when (screenMode) {
                                        ScreenMode.ADD_MODE -> AlarmEntity.UNDEFINED_ID
                                        ScreenMode.EDIT_MODE -> alarmId
                                    }
                                )
                            }

                            is AlarmStates.DataLoaded -> {
                                timePickerAlarm.hour = it.hour
                                timePickerAlarm.minute = it.minute
                            }

                            AlarmStates.Loading -> {
                                progressBarLoading.visibility = View.VISIBLE
                                textViewSave.isEnabled = false
                                timePickerAlarm.isEnabled = false
                            }

                            AlarmStates.Success -> onEndWorkListener.onEndWork()
                        }
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

                alarmId = getInt(ALARM_ID)
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

    interface OnEndWorkListener {
        fun onEndWork()
    }

    companion object {
        const val BACKSTACK_NAME = "alarm_fragment"

        private const val SCREEN_MODE = "screen_mode"
        private const val ALARM_ID = "alarm_id"

        private const val UNDEFINED_ID = 0

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