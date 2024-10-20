package com.bloodspy.clockly.presentation.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.bloodspy.clockly.R
import com.bloodspy.clockly.databinding.ActivityClocklyBinding
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.presentation.fragments.AlarmFragment
import com.bloodspy.clockly.presentation.fragments.AlarmsFragment

class ClocklyActivity : AppCompatActivity(),
    AlarmsFragment.OnAddButtonClickListener,
    AlarmsFragment.OnAlarmClickListener,
    AlarmFragment.OnEndWorkListener {
    private val binding by lazy {
        ActivityClocklyBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clockly)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(binding.alarmsContainer.id, AlarmsFragment())
                .commit()
        }
    }

    override fun onAddButtonClick() {
        supportFragmentManager.beginTransaction()
            .replace(binding.alarmsContainer.id, AlarmFragment.newInstanceAddMode())
            .addToBackStack(AlarmFragment.BACKSTACK_NAME)
            .commit()
    }

    override fun onAlarmClick(alarmEntity: AlarmEntity) {
        supportFragmentManager.beginTransaction()
            .replace(
                binding.alarmsContainer.id,
                AlarmFragment.newInstanceEditMode(alarmEntity.id)
            )
            .addToBackStack(AlarmFragment.BACKSTACK_NAME)
            .commit()
    }

    override fun onEndWork() {
        onBackPressedDispatcher.onBackPressed()
    }
}