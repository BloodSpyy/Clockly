package com.bloodspy.clockly.di

import android.app.Application
import com.bloodspy.clockly.di.modules.DataModule
import com.bloodspy.clockly.di.modules.DomainModule
import com.bloodspy.clockly.di.modules.ViewModelModule
import com.bloodspy.clockly.di.scopes.ClocklyAppScope
import com.bloodspy.clockly.presentation.fragments.AlarmsFragment
import dagger.BindsInstance
import dagger.Component

@ClocklyAppScope
@Component(modules = [DataModule::class, DomainModule::class, ViewModelModule::class])
interface ClocklyComponent {

    fun inject(alarmsFragment: AlarmsFragment)

    @Component.Factory
    interface ClocklyComponentFactory {
        fun create(@BindsInstance application: Application): ClocklyComponent
    }
}