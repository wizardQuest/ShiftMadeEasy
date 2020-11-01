package com.pq.shiftmadeeasy.localdatabase

import androidx.lifecycle.LiveData
import com.pq.shiftmadeeasy.localdatabase.shift.Shift
import com.pq.shiftmadeeasy.localdatabase.shift.ShiftDao
import com.pq.shiftmadeeasy.localdatabase.task.Task
import com.pq.shiftmadeeasy.localdatabase.task.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val taskDao: TaskDao,
    private val shiftDao: ShiftDao
) {

    //Tasks
    suspend fun insertTaskLocally(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.insert(task)
        }
    }

    suspend fun updateTaskLocally(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.update(task)
        }
    }

    suspend fun deleteTaskLocally(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.delete(task)
        }
    }

    fun getAllTasks(): LiveData<List<Task>> {
        return taskDao.getAllTasks()
    }

    //Shifts
    suspend fun insertShiftLocally(shift: Shift) {
        withContext(Dispatchers.IO) {
            shiftDao.insert(shift)
        }
    }

    suspend fun updateShiftLocally(shift: Shift) {
        withContext(Dispatchers.IO) {
            shiftDao.update(shift)
        }
    }

    suspend fun deleteShiftLocally(shift: Shift) {
        withContext(Dispatchers.IO) {
            shiftDao.delete(shift)
        }
    }

    fun getAllShifts(): LiveData<MutableList<Shift>> {
        return shiftDao.getAllShifts()
    }
}