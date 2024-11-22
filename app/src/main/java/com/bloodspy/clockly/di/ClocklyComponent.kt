package com.bloodspy.clockly.di

import android.app.Application
import com.bloodspy.clockly.AppApplication
import com.bloodspy.clockly.di.modules.DataModule
import com.bloodspy.clockly.di.modules.DomainModule
import com.bloodspy.clockly.di.modules.ViewModelModule
import com.bloodspy.clockly.di.scopes.ClocklyAppScope
import com.bloodspy.clockly.presentation.fragments.AlarmFragment
import com.bloodspy.clockly.presentation.fragments.AlarmsFragment
import com.bloodspy.clockly.services.AlarmService
import dagger.BindsInstance
import dagger.Component

//todo попробуй внедрить daggerInjection
@ClocklyAppScope
@Component(
    modules = [
        DataModule::class,
        DomainModule::class,
        ViewModelModule::class,
    ]
)
interface ClocklyComponent {
    fun inject(alarmsFragment: AlarmsFragment)

    fun inject(alarmFragment: AlarmFragment)

    fun inject(alarmService: AlarmService)

    @Component.Factory
    interface ClocklyComponentFactory {
        fun create(@BindsInstance application: Application): ClocklyComponent
    }
}