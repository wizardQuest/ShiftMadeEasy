package com.pq.shiftmadeeasy.localdatabase.shift

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.pq.shiftmadeeasy.localdatabase.BaseDao

@Dao
interface ShiftDao : BaseDao<Shift> {
    @Query("select * from shift_table")
    fun getAllShifts(): LiveData<MutableList<Shift>>
}