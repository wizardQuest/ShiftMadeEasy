package com.pq.shiftmadeeasy.repository.task

import androidx.lifecycle.LiveData
import com.pq.focuslist.repository.task.TaskRepository
import com.pq.shiftmadeeasy.localdatabase.UserLocalDataSource
import com.pq.shiftmadeeasy.localdatabase.task.Task
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val userLocalDataSource: UserLocalDataSource) :
    TaskRepository {

    override suspend fun insert(task: Task) {
        userLocalDataSource.insertTaskLocally(task)
    }

    override suspend fun update(task: Task) {
        userLocalDataSource.updateTaskLocally(task)
    }

    override suspend fun delete(task: Task) {
        userLocalDataSource.deleteTaskLocally(task)
    }

    override fun getAllTasks(): LiveData<List<Task>> {
        return userLocalDataSource.getAllTasks()
    }
}