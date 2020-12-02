package com.pq.shiftmadeeasy.localdatabase.task

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var taskId: Long = 0L,

    var taskDate: String? = "",

    var taskFromTime: String?,

    var taskToTime: String? = "",

    var taskLoggedHours: String? = "",

    var position: String? = null,

    var taskTimeInMillisecond: Long = 0
) : Parcelable