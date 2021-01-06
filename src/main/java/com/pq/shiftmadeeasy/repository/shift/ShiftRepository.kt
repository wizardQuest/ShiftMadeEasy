package com.pq.shiftmadeeasy.repository.shift

import com.pq.shiftmadeeasy.localdatabase.shift.Shift
import com.pq.shiftmadeeasy.localdatabase.shift.ShiftWithCalendars
import kotlinx.coroutines.flow.Flow

interface ShiftRepository {
    suspend fun insert(shift: Shift)
    suspend fun update(shift: Shift)
    suspend fun delete(shift: Shift)
    fun getAllShifts(): Flow<MutableList<Shift>>
    suspend fun getAllShiftsWithCalendar(): Flow<MutableList<ShiftWithCalendars>>
}