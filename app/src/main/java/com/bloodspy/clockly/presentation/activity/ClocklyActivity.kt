package com.bloodspy.clockly.presentation.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bloodspy.clockly.R
import com.bloodspy.clockly.databinding.ActivityClocklyBinding
import com.bloodspy.clockly.domain.entities.AlarmEntity
import com.bloodspy.clockly.presentation.fragments.AlarmFragment
import com.bloodspy.clockly.presentation.fragments.AlarmsFragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class ClocklyActivity : AppCompatActivity(),
    AlarmsFragment.OnAddButtonClickListener,
    AlarmsFragment.OnAlarmClickListener,
    AlarmFragment.OnEndWorkListener {
    private val binding by lazy {
        ActivityClocklyBinding.inflate(layoutInflater)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted -> handleGrantedPermission(isGranted) }

    private val openSettingsForEnableNotificationsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { handleGrantedPermission(isNotificationsPermissionGranted()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        startApp(savedInstanceState)
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

    private fun startApp(savedInstanceState: Bundle?) {
        if (!isNotificationsPermissionGranted()) {
            requestNotificationsPermission()
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(binding.alarmsContainer.id, AlarmsFragment.newInstance())
                .commit()
        }
    }

    private fun isNotificationsPermissionGranted(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            NOTIFICATION_ALLOW_BY_DEFAULT
        }

    private fun requestNotificationsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }

    private fun handleGrantedPermission(isGranted: Boolean) {
        if (!isGranted) {
            showSnackbar()
        }
    }

    private fun showSnackbar() {
        Snackbar.make(
            this,
            binding.root,
            getString(R.string.snackbar_message),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .setAction(getString(R.string.snackbar_action)) {
                openSettingsForEnableNotificationsLauncher.launch(
                    getIntentToAppSettings()
                )
            }
            .show()
    }

    private fun getIntentToAppSettings(): Intent {
        return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:${applicationContext.packageName}")
        }
    }

    companion object {
        private const val NOTIFICATION_ALLOW_BY_DEFAULT = true
    }
}
