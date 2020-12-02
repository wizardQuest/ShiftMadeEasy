package com.pq.shiftmadeeasy.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pq.shiftmadeeasy.ui.addtodobottomsheet.AddTodoTaskViewModel
import com.pq.shiftmadeeasy.ui.calendarview.ShiftAndCalendarRepositoryViewModel
import com.pq.shiftmadeeasy.ui.newuserlanding.NewUserLandingViewModel
import com.pq.shiftmadeeasy.ui.viewmodels.RepositoryViewModel
import com.pq.shiftmadeeasy.ui.viewmodels.ViewModelProviderFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.multibindings.IntoMap

@InstallIn(ApplicationComponent::class)
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RepositoryViewModel::class)
    abstract fun bindRepositoryViewModel(repositoryViewModel: RepositoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddTodoTaskViewModel::class)
    abstract fun bindAddTodoViewModel(repositoryViewModel: AddTodoTaskViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewUserLandingViewModel::class)
    abstract fun bindNewUserLandingViewModel(repositoryViewModel: NewUserLandingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShiftAndCalendarRepositoryViewModel::class)
    abstract fun bindShiftRepositoryViewModel(andCalendarRepositoryViewModel: ShiftAndCalendarRepositoryViewModel): ViewModel
    /*
    @Binds
    @IntoMap
    @ViewModelKey(UserHomeViewModel::class)
    abstract fun bindUserHomeViewModel(userHomeViewModel: UserHomeViewModel): ViewModel*/

    @Binds
    abstract fun bindViewModelFactory(viewModelProvideFactory: ViewModelProviderFactory): ViewModelProvider.Factory
}