package com.pq.shiftmadeeasy.util

import com.pq.shiftmadeeasy.localdatabase.shift.Shift
import com.pq.shiftmadeeasy.localdatabase.task.Task

fun getTaskListInOrder(taskList: List<Task>): List<Task> {
    return taskList.sortedBy { it.taskTimeInMillisecond }
}

fun getShiftListInOrder(shiftList: List<Shift>): List<Shift> {
    return shiftList.sortedBy { it.shiftIndex }
}