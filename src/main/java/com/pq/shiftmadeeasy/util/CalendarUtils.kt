package com.pq.shiftmadeeasy.util

import java.util.*

fun getCalendarDateFromMilliseconds(timeInMilliSeconds: Long): Calendar {
    val date = Date(timeInMilliSeconds)
    var calendar = Calendar.getInstance()
    calendar.time = date
    return calendar
}