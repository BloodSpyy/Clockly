package com.bloodspy.clockly.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bloodspy.clockly.R
import com.bloodspy.clockly.databinding.ActivityClocklyBinding
import com.bloodspy.clockly.presentation.fragments.AlarmFragment

class ClocklyActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityClocklyBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clockly)

        if(savedInstanceState == null) {
            startAlarmFragment()
        }
    }

    private fun startAlarmFragment() {
        supportFragmentManager.beginTransaction()
            .replace(binding.clocklyContainer.id, AlarmFragment.newInstance(this))
            .commit()
            //todo потести и реши, нужно ли тут добавлять его в backstack
    }
}