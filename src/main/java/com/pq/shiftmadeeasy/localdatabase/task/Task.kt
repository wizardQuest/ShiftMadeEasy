package com.pq.shiftmadeeasy.localdatabase.task

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(taskId)
        parcel.writeString(taskDate)
        parcel.writeString(taskFromTime)
        parcel.writeString(taskToTime)
        parcel.writeString(taskLoggedHours)
        parcel.writeString(position)
        parcel.writeLong(taskTimeInMillisecond)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}