package com.bloodspy.clockly.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bloodspy.clockly.AppApplication
import com.bloodspy.clockly.databinding.FragmentAlarmBinding
import com.bloodspy.clockly.presentation.viewmodels.AlarmsViewModel
import com.bloodspy.clockly.presentation.viewmodels.factory.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlarmFragment : Fragment() {
    private var _binding: FragmentAlarmBinding? = null
    private val binding: FragmentAlarmBinding
        get() = _binding ?: throw RuntimeException("FragmentAlarmBinding is null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AlarmsViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        injectDependency()

        super.onAttach(context)
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

        observeViewModel()
        setupOnClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun observeViewModel() {
        observeAlarms()
    }

    private fun observeAlarms() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.alarms().collect {
                    //todo делай submit list в recycler view
                }
            }
        }
    }

    private fun setupOnClickListeners() {
        setupAddAlarmClickListener()
        setupStopwatchClickListener()
        setupWeatherClickListener()
    }

    private fun setupAddAlarmClickListener() {
        binding.imageViewAddAlarm.setOnClickListener {
            //todo делай интент в AlarmItemFragment
        }
    }

    private fun setupStopwatchClickListener() {
        binding.imageButtonStopwatch.setOnClickListener {
            //todo делай интент в StopwatchFragment
        }
    }

    private fun setupWeatherClickListener() {
        binding.imageButtonWeather.setOnClickListener {
            //todo делай интент в WeatehFragment
        }
    }

    private fun injectDependency() {
        (requireActivity().application as AppApplication).component
            .inject(this)
    }

    companion object {
        fun newInstance(context: Context): AlarmFragment = AlarmFragment()
    }
}