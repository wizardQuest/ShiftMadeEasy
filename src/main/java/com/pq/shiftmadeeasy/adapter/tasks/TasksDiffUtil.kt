package com.pq.shiftmadeeasy.adapter.tasks

import androidx.recyclerview.widget.DiffUtil
import com.pq.shiftmadeeasy.localdatabase.task.Task

class TasksDiffUtil : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem == newItem
}