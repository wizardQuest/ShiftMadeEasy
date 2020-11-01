package com.pq.shiftmadeeasy.localdatabase.shift

import android.graphics.Color
import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "shift_table")
@Keep
@Parcelize
data class Shift(
    @PrimaryKey(autoGenerate = true)
    var projectId: Long = 0L,
    var shiftTitle: String = "",
    var shiftColor: Int = Color.BLACK,
    var shiftAlphabet: String = "A",
    var shiftIndex: Int = 0
) : Parcelable