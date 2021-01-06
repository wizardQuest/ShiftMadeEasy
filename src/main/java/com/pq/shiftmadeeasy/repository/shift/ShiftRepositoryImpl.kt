package com.pq.shiftmadeeasy.repository.shift

import androidx.lifecycle.LiveData
import com.pq.shiftmadeeasy.localdatabase.UserLocalDataSource
import com.pq.shiftmadeeasy.localdatabase.shift.Shift
import com.pq.shiftmadeeasy.localdatabase.shift.ShiftWithCalendars
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShiftRepositoryImpl @Inject constructor(private val userLocalDataSource: UserLocalDataSource) :
    ShiftRepository {
    override suspend fun insert(shift: Shift) {
        userLocalDataSource.insertShiftLocally(shift)
    }

    override suspend fun update(shift: Shift) {
        userLocalDataSource.updateShiftLocally(shift)
    }

    override suspend fun delete(shift: Shift) {
        userLocalDataSource.deleteShiftLocally(shift)
    }

    override fun getAllShifts(): Flow<MutableList<Shift>> {
        return userLocalDataSource.getAllShifts()
    }

    override suspend fun getAllShiftsWithCalendar(): Flow<MutableList<ShiftWithCalendars>> {
        return userLocalDataSource.getAllShiftsWithCalendar()
    }
}