package com.pq.shiftmadeeasy.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pq.shiftmadeeasy.localdatabase.task.Task
import com.pq.shiftmadeeasy.repository.task.TaskRepositoryImpl
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class RepositoryViewModel @Inject constructor(
    private val taskRepository: TaskRepositoryImpl
) : ViewModel() {

    private var _taskList = MutableLiveData<List<Task>>()
    val taskList: LiveData<List<Task>>
        get() = _taskList

    private var _reminderCalender = MutableLiveData<Calendar>()
    val reminderCalender: LiveData<Calendar>
        get() = _reminderCalender

    fun insert(task: Task) {
        viewModelScope.launch {
            taskRepository.insert(task)
        }
    }

    fun update(task: Task) {
        viewModelScope.launch {
            taskRepository.update(task)
        }
    }

    fun delete(task: Task) {
        viewModelScope.launch {
            taskRepository.delete(task)
        }
    }

    fun getAllTasks(): LiveData<List<Task>> {
        return taskRepository.getAllTasks()
    }

    fun setReminderCalender(calendar: Calendar) {
        _reminderCalender.value = calendar
    }
}