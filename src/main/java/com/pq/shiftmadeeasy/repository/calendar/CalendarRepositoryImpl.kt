package com.pq.shiftmadeeasy.repository.calendar

import androidx.lifecycle.LiveData
import com.pq.shiftmadeeasy.localdatabase.UserLocalDataSource
import com.pq.shiftmadeeasy.localdatabase.calendarwithshift.CustomCalendar
import com.pq.shiftmadeeasy.localdatabase.calendarwithshift.CustomCalendarForRepeatingShifts
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(private val userLocalDataSource: UserLocalDataSource) :
    CalendarRepository {
    /**--------------------Calendar------------------------------*/
    override suspend fun insert(calendar: CustomCalendar) {
        userLocalDataSource.insertCalendarLocally(calendar)
    }

    override suspend fun update(calendar: CustomCalendar) {
        userLocalDataSource.updateCalendarLocally(calendar)
    }

    override suspend fun delete(calendar: CustomCalendar) {
        userLocalDataSource.deleteCalendarLocally(calendar)
    }

    override fun getAllCalendars(): LiveData<MutableList<CustomCalendar>> {
        return userLocalDataSource.getAllCalendars()
    }
    override suspend fun deleteAllCalendars() {
        TODO("Not yet implemented")
    }

    /**--------------------Calendar for Repeating Shifts------------------------------*/
    override suspend fun insert(calendar: CustomCalendarForRepeatingShifts) {
        userLocalDataSource.insertCalendarLocally(calendar)
    }

    override suspend fun update(calendar: CustomCalendarForRepeatingShifts) {
        userLocalDataSource.updateCalendarLocally(calendar)
    }

    override suspend fun delete(calendar: CustomCalendarForRepeatingShifts) {
        userLocalDataSource.deleteCalendarLocally(calendar)
    }

    override suspend fun updateCalendarForRepeatingShifts(calendarList: List<CustomCalendarForRepeatingShifts>) {
        userLocalDataSource.updateCalendarForRepeatingShifts(calendarList)
    }

    override fun getAllCalendarsForRepeatingShifts(): LiveData<MutableList<CustomCalendarForRepeatingShifts>> {
        return userLocalDataSource.getAllCalendarsForRepeatingShifts()
    }
}