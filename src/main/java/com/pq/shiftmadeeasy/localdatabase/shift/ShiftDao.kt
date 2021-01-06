package com.pq.shiftmadeeasy.localdatabase.shift

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.pq.shiftmadeeasy.localdatabase.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface ShiftDao : BaseDao<Shift> {
    @Query("select * from shift_table")
    fun getAllShifts(): Flow<MutableList<Shift>>

    @Transaction
    @Query("SELECT * FROM shift_table")
    fun getAllShiftsWithCalendar(): Flow<MutableList<ShiftWithCalendars>>
}