package com.bloodspy.clockly.di.modules

import androidx.lifecycle.ViewModel
import com.bloodspy.clockly.di.keys.ViewModelKey
import com.bloodspy.clockly.presentation.viewmodels.AlarmsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AlarmsViewModel::class)
    fun bindAlarmsViewModel(alarmsViewModel: AlarmsViewModel): ViewModel
}