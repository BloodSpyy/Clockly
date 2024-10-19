package com.bloodspy.clockly.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bloodspy.clockly.R
import com.bloodspy.clockly.databinding.ActivityClocklyBinding
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.presentation.fragments.AlarmFragment
import com.bloodspy.clockly.presentation.fragments.AlarmsFragment

class ClocklyActivity : AppCompatActivity(),
    AlarmsFragment.OnAddButtonClickListener,
    AlarmsFragment.OnAlarmClickListener {
    private val binding by lazy {
        ActivityClocklyBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clockly)

        if (savedInstanceState == null) {
            with(AlarmsFragment) {
                startFragment(newInstance(), null)
            }
        }
    }

    override fun onAddButtonClick() {
        with(AlarmFragment) {
            startFragment(newInstanceAddMode(), BACKSTACK_NAME)
        }
    }

    override fun onAlarmClick(alarmEntity: AlarmEntity) {
        with(AlarmFragment) {
            startFragment(newInstanceEditMode(alarmEntity.id), BACKSTACK_NAME)
        }
    }

    private fun startFragment(fragment: Fragment, nameForBackStack: String?) {
        with(supportFragmentManager) {
            val transaction = beginTransaction()
                .replace(binding.alarmsContainer.id, fragment)

            if (nameForBackStack != null) {
                transaction.addToBackStack(nameForBackStack)
            }

            transaction.commit()
        }
    }


}