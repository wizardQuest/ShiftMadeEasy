package com.pq.shiftmadeeasy.localdatabase.task

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.pq.shiftmadeeasy.localdatabase.BaseDao

@Dao
interface TaskDao : BaseDao<Task> {
    @Query("select * from task_table")
    fun getAllTasks(): LiveData<List<Task>>
}