package com.pq.shiftmadeeasy.data

import android.os.Parcel
import android.os.Parcelable

data class TimeStamp(
    var date: Int? = null,
    var month: Int? = null,
    var year: Int? = null,
    var hour: Int? = null,
    var minute: Int? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(date)
        parcel.writeValue(month)
        parcel.writeValue(year)
        parcel.writeValue(hour)
        parcel.writeValue(minute)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TimeStamp> {
        override fun createFromParcel(parcel: Parcel): TimeStamp {
            return TimeStamp(parcel)
        }

        override fun newArray(size: Int): Array<TimeStamp?> {
            return arrayOfNulls(size)
        }
    }
}