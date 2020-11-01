package com.pq.shiftmadeeasy.repository.shift

import androidx.lifecycle.LiveData
import com.pq.shiftmadeeasy.localdatabase.UserLocalDataSource
import com.pq.shiftmadeeasy.localdatabase.shift.Shift
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

    override fun getAllShifts(): LiveData<MutableList<Shift>> {
        return userLocalDataSource.getAllShifts()
    }
}