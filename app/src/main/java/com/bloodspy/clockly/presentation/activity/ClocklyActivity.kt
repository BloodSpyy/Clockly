package com.bloodspy.clockly.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bloodspy.clockly.R
import com.bloodspy.clockly.databinding.ActivityClocklyBinding
import com.bloodspy.clockly.presentation.fragments.AlarmFragment
import com.bloodspy.clockly.presentation.fragments.AlarmsFragment

class ClocklyActivity : AppCompatActivity(), AlarmsFragment.OnClickAddButtonListener {
    private val binding by lazy {
        ActivityClocklyBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clockly)

        if(savedInstanceState == null) {
            startFragment(AlarmsFragment.newInstance())
        }
    }

    override fun onClickAddButton() {
        startFragment(AlarmFragment.newInstanceAddMode())
    }

    private fun startFragment(fragment: Fragment) {
            supportFragmentManager.beginTransaction()
                .replace(binding.alarmsContainer.id, fragment)
                .commit()
            //todo потести и реши, нужно ли тут добавлять его в backstack
    }
}