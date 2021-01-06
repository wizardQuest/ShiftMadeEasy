package com.pq.shiftmadeeasy.localdatabase.calendarwithshift

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.pq.shiftmadeeasy.localdatabase.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomCalendarDao : BaseDao<CustomCalendar> {
    @Query("select * from calendar_table")
    fun getAllCalendars(): Flow<MutableList<CustomCalendar>>
}

@Dao
interface CustomCalendarForRepeatingShiftsDao : BaseDao<CustomCalendarForRepeatingShifts> {
    @Query("select * from calendar_table_for_repeating_shifts")
    fun getAllCalendars(): Flow<MutableList<CustomCalendarForRepeatingShifts>>

    @Transaction
    open fun updateData(users: List<CustomCalendarForRepeatingShifts>) {
        deleteAllUsers()
        insertAll(users)
    }

    @Insert
    abstract fun insertAll(users: List<CustomCalendarForRepeatingShifts>)

    @Query("DELETE FROM calendar_table_for_repeating_shifts")
    abstract fun deleteAllUsers()
}