package com.pq.shiftmadeeasy.localdatabase.shift

import android.graphics.Color
import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.pq.shiftmadeeasy.localdatabase.calendarwithshift.CustomCalendar
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "shift_table")
@Keep
@Parcelize
data class Shift(
    @PrimaryKey(autoGenerate = true)
    var shiftId: Long = 0L,
    var shiftTitle: String = "",
    var shiftColor: Int = Color.BLACK,
    var shiftShortForm: String = "A",
    var shiftIndex: Int = 0
) : Parcelable

data class ShiftWithCalendars(
    @Embedded val shift: Shift,
    @Relation(
        parentColumn = "shiftId",
        entityColumn = "shiftId"
    )
    val calendarList: List<CustomCalendar>
)