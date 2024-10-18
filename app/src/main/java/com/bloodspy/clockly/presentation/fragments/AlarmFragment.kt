package com.bloodspy.clockly.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bloodspy.clockly.databinding.FragmentAlarmBinding
import com.bloodspy.clockly.domain.entities.AlarmEntity

class AlarmFragment : Fragment() {
    private var _binding: FragmentAlarmBinding? = null
    private val binding: FragmentAlarmBinding
        get() = _binding ?: throw RuntimeException("FragmentAlarmBinding is null")

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

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    companion object {
        private const val EXTRA_ALARM = "alarm"

        fun newInstance(alarm: AlarmEntity): AlarmFragment {
            return AlarmFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(EXTRA_ALARM, alarm)
                }
            }
        }
    }
}