package com.pq.shiftmadeeasy.di

import androidx.lifecycle.ViewModelProvider
import com.pq.shiftmadeeasy.ui.viewmodels.ViewModelProviderFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelProvideFactory: ViewModelProviderFactory): ViewModelProvider.Factory
}