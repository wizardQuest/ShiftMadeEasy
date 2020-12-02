package com.pq.shiftmadeeasy.localdatabase.calendarwithshift

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/** This is used for editing the current calendar's shift. Like user can set a shift for a particular day**/
@Entity(tableName = "calendar_table")
@Keep
@Parcelize
data class CustomCalendar(
    @PrimaryKey(autoGenerate = true)
    var calendarIndex: Long = 0L,
    var calendarTime: Long,
    var shiftId: Long?
) : Parcelable


/** This is used by user to set set of shifts in a repeating fashion for any amount of time**/
@Entity(tableName = "calendar_table_for_repeating_shifts")
@Keep
@Parcelize
data class CustomCalendarForRepeatingShifts(
    @PrimaryKey(autoGenerate = true)
    var calendarIndex: Long = 0L,
    var calendarTime: Long,
    var shiftId: Long?
) : Parcelable

