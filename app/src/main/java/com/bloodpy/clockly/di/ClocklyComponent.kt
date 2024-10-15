package com.bloodpy.clockly.di

import android.app.Application
import com.bloodpy.clockly.di.modules.DataModule
import com.bloodpy.clockly.di.modules.DomainModule
import com.bloodpy.clockly.di.scopes.ApplicationScope
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, DomainModule::class])
interface ClocklyComponent {

    companion object {
        @Component.Factory
        interface ApplicationComponentFactory {
            fun create(@BindsInstance application: Application): ClocklyComponent
        }
    }
}