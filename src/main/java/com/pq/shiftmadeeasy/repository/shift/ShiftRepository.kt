package com.pq.shiftmadeeasy.repository.shift

import androidx.lifecycle.LiveData
import com.pq.shiftmadeeasy.localdatabase.shift.Shift
import com.pq.shiftmadeeasy.localdatabase.shift.ShiftWithCalendars

interface ShiftRepository {
    suspend fun insert(shift: Shift)
    suspend fun update(shift: Shift)
    suspend fun delete(shift: Shift)
    fun getAllShifts(): LiveData<MutableList<Shift>>
    suspend fun getAllShiftsWithCalendar(): LiveData<MutableList<ShiftWithCalendars>>
}