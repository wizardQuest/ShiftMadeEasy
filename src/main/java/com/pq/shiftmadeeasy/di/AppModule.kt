package com.pq.shiftmadeeasy.di

import android.app.Application
import android.content.Context
import com.pq.shiftmadeeasy.localdatabase.AppDatabase
import com.pq.shiftmadeeasy.localdatabase.UserLocalDataSource
import com.pq.shiftmadeeasy.localdatabase.calendarwithshift.CustomCalendar
import com.pq.shiftmadeeasy.localdatabase.calendarwithshift.CustomCalendarDao
import com.pq.shiftmadeeasy.localdatabase.calendarwithshift.CustomCalendarForRepeatingShiftsDao
import com.pq.shiftmadeeasy.localdatabase.shift.ShiftDao
import com.pq.shiftmadeeasy.localdatabase.task.TaskDao
import com.pq.shiftmadeeasy.repository.calendar.CalendarRepository
import com.pq.shiftmadeeasy.repository.calendar.CalendarRepositoryImpl
import com.pq.shiftmadeeasy.repository.shift.ShiftRepository
import com.pq.shiftmadeeasy.repository.shift.ShiftRepositoryImpl
import com.pq.shiftmadeeasy.repository.task.TaskRepositoryImpl
import com.pq.shiftmadeeasy.settings.SettingsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun providesAppDatabase(app: Application): AppDatabase {
        return AppDatabase.getInstance(app)
    }

    @Singleton
    @Provides
    fun providesTasksDao(database: AppDatabase): TaskDao {
        return database.taskDao
    }

    @Singleton
    @Provides
    fun providesShiftsDao(database: AppDatabase): ShiftDao {
        return database.shiftDao
    }

    @Singleton
    @Provides
    fun providesCustomCalendarDao(database: AppDatabase): CustomCalendarDao {
        return database.customCalendarDao
    }

    @Singleton
    @Provides
    fun providesCustomCalendarForRepeatingShiftsDao(database: AppDatabase): CustomCalendarForRepeatingShiftsDao {
        return database.customCalendarForRepeatingShiftsDao
    }

    @Singleton
    @Provides
    fun providesTaskRepository(userLocalDataSource: UserLocalDataSource): TaskRepositoryImpl {
        return TaskRepositoryImpl(
            userLocalDataSource
        )
    }

    @Singleton
    @Provides
    fun providesShiftRepository(userLocalDataSource: UserLocalDataSource): ShiftRepository {
        return ShiftRepositoryImpl(
            userLocalDataSource
        )
    }

    @Singleton
    @Provides
    fun providesCalendarRepository(userLocalDataSource: UserLocalDataSource): CalendarRepository {
        return CalendarRepositoryImpl(
            userLocalDataSource
        )
    }

    @Singleton
    @Provides
    fun providesSettingsManager(context: Context): SettingsManager {
        return SettingsManager(context)
    }

    @Singleton
    @Provides
    fun providesContext(@ApplicationContext context: Context): Context {
        return context
    }
}