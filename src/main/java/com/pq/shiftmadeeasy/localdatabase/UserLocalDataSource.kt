package com.pq.shiftmadeeasy.localdatabase

import androidx.lifecycle.LiveData
import com.pq.shiftmadeeasy.localdatabase.calendarwithshift.CustomCalendar
import com.pq.shiftmadeeasy.localdatabase.calendarwithshift.CustomCalendarDao
import com.pq.shiftmadeeasy.localdatabase.calendarwithshift.CustomCalendarForRepeatingShifts
import com.pq.shiftmadeeasy.localdatabase.calendarwithshift.CustomCalendarForRepeatingShiftsDao
import com.pq.shiftmadeeasy.localdatabase.shift.Shift
import com.pq.shiftmadeeasy.localdatabase.shift.ShiftDao
import com.pq.shiftmadeeasy.localdatabase.shift.ShiftWithCalendars
import com.pq.shiftmadeeasy.localdatabase.task.Task
import com.pq.shiftmadeeasy.localdatabase.task.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val taskDao: TaskDao,
    private val shiftDao: ShiftDao,
    private val customCalendarDao: CustomCalendarDao,
    private val customCalendarForRepeatingShiftsDao: CustomCalendarForRepeatingShiftsDao
) {

    /**--------------------Tasks------------------------------*/
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

    /**--------------------Shifts------------------------------*/
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

    fun getAllShifts(): Flow<MutableList<Shift>> {
        return shiftDao.getAllShifts()
    }

    fun getAllShiftsWithCalendar(): Flow<MutableList<ShiftWithCalendars>> {
        return shiftDao.getAllShiftsWithCalendar()
    }

    /**--------------------CustomCalendar------------------------------*/
    suspend fun insertCalendarLocally(calendar: CustomCalendar) {
        withContext(Dispatchers.IO) {
            customCalendarDao.insert(calendar)
        }
    }

    suspend fun updateCalendarLocally(calendar: CustomCalendar) {
        withContext(Dispatchers.IO) {
            customCalendarDao.update(calendar)
        }
    }

    suspend fun deleteCalendarLocally(calendar: CustomCalendar) {
        withContext(Dispatchers.IO) {
            customCalendarDao.delete(calendar)
        }
    }

    fun getAllCalendars(): Flow<MutableList<CustomCalendar>> {
        return customCalendarDao.getAllCalendars()
    }

    /**--------------------Custom Calendar For Repeating Shifts------------------------------*/
    suspend fun insertCalendarLocally(calendar: CustomCalendarForRepeatingShifts) {
        withContext(Dispatchers.IO) {
            customCalendarForRepeatingShiftsDao.insert(calendar)
        }
    }

    suspend fun updateCalendarLocally(calendar: CustomCalendarForRepeatingShifts) {
        withContext(Dispatchers.IO) {
            customCalendarForRepeatingShiftsDao.update(calendar)
        }
    }

    suspend fun deleteCalendarLocally(calendar: CustomCalendarForRepeatingShifts) {
        withContext(Dispatchers.IO) {
            customCalendarForRepeatingShiftsDao.delete(calendar)
        }
    }

    fun getAllCalendarsForRepeatingShifts(): Flow<MutableList<CustomCalendarForRepeatingShifts>> {
        return customCalendarForRepeatingShiftsDao.getAllCalendars()
    }

    suspend fun updateCalendarForRepeatingShifts(calendarList: List<CustomCalendarForRepeatingShifts>) {
        withContext(Dispatchers.IO) {
            customCalendarForRepeatingShiftsDao.updateData(calendarList)
        }
    }
}