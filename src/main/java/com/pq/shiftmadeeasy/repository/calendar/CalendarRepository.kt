package com.pq.shiftmadeeasy.repository.calendar

import androidx.lifecycle.LiveData
import com.pq.shiftmadeeasy.localdatabase.calendarwithshift.CustomCalendar
import com.pq.shiftmadeeasy.localdatabase.calendarwithshift.CustomCalendarForRepeatingShifts
import com.pq.shiftmadeeasy.localdatabase.shift.Shift
import kotlinx.coroutines.flow.Flow

interface CalendarRepository {
    suspend fun insert(calendar: CustomCalendar)
    suspend fun update(calendar: CustomCalendar)
    suspend fun delete(calendar: CustomCalendar)
    suspend fun deleteAllCalendars()
    fun getAllCalendars(): Flow<MutableList<CustomCalendar>>

    suspend fun insert(calendar: CustomCalendarForRepeatingShifts)
    suspend fun update(calendar: CustomCalendarForRepeatingShifts)
    suspend fun delete(calendar: CustomCalendarForRepeatingShifts)
    fun getAllCalendarsForRepeatingShifts(): Flow<MutableList<CustomCalendarForRepeatingShifts>>
    suspend fun updateCalendarForRepeatingShifts(calendarList: List<CustomCalendarForRepeatingShifts>)
}