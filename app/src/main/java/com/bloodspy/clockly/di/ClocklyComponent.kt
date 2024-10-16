package com.bloodspy.clockly.di

import android.app.Application
import com.bloodspy.clockly.di.modules.DataModule
import com.bloodspy.clockly.di.modules.DomainModule
import com.bloodspy.clockly.di.scopes.ClocklyAppScope
import dagger.BindsInstance
import dagger.Component

@ClocklyAppScope
@Component(modules = [DataModule::class, DomainModule::class])
interface ClocklyComponent {

    @Component.Factory
    interface ClocklyComponentFactory {
        fun create(@BindsInstance application: Application): ClocklyComponent
    }
}