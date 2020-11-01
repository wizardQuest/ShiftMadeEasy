package com.pq.shiftmadeeasy.repository.shift

import androidx.lifecycle.LiveData
import com.pq.shiftmadeeasy.localdatabase.shift.Shift

interface ShiftRepository {
    suspend fun insert(shift: Shift)
    suspend fun update(shift: Shift)
    suspend fun delete(shift: Shift)
    fun getAllShifts(): LiveData<MutableList<Shift>>
}