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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bloodspy.clockly.AppApplication
import com.bloodspy.clockly.databinding.FragmentAlarmBinding
import com.bloodspy.clockly.databinding.FragmentAlarmsBinding
import com.bloodspy.clockly.presentation.recyclerViewUtils.adapter.AlarmsAdapter
import com.bloodspy.clockly.presentation.viewmodels.AlarmsViewModel
import com.bloodspy.clockly.presentation.viewmodels.factory.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlarmsFragment : Fragment() {
    private var _binding: FragmentAlarmsBinding? = null
    private val binding: FragmentAlarmsBinding
        get() = _binding ?: throw RuntimeException("FragmentAlarmBinding is null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AlarmsViewModel::class.java]
    }

    private val alarmsAdapter by lazy { AlarmsAdapter() }

    override fun onAttach(context: Context) {
        injectDependency()

        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAlarmsBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        setupRecyclerView()
        setupAdapterListeners()
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
                    alarmsAdapter.submitList(it)
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

    private fun setupRecyclerView() {
        binding.recyclerViewAlarms.adapter = alarmsAdapter
    }

    private fun setupAdapterListeners() {
        setupOnChangedEnableState()
        setupSwipeListener()
    }

    private fun setupOnChangedEnableState() {
        alarmsAdapter.onChangedEnableState = {
            viewModel.changeEnableState(it)
        }
    }

    private fun setupSwipeListener() {
        val callback = object : ItemTouchHelper.SimpleCallback(
            NOT_USED,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val alarm = alarmsAdapter.currentList[viewHolder.adapterPosition]

                viewModel.deleteAlarm(alarm)
            }
        }

        ItemTouchHelper(callback).attachToRecyclerView(binding.recyclerViewAlarms)
    }

    companion object {
        private const val NOT_USED = 0

        fun newInstance(): AlarmsFragment = AlarmsFragment()
    }
}