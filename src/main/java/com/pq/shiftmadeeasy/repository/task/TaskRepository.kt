package com.pq.focuslist.repository.task

import androidx.lifecycle.LiveData
import com.pq.shiftmadeeasy.localdatabase.task.Task

interface TaskRepository {
    suspend fun insert(task: Task)
    suspend fun update(task: Task)
    suspend fun delete(task: Task)
    fun getAllTasks(): LiveData<List<Task>>
}