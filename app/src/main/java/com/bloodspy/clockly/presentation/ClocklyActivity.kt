package com.bloodspy.clockly.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bloodspy.clockly.R
import com.bloodspy.clockly.databinding.ActivityClocklyBinding

class ClocklyActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityClocklyBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clockly)
    }
}