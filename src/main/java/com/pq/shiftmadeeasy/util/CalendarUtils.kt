package com.pq.shiftmadeeasy.util

import java.util.*

fun getCalendarDateFromMilliseconds(timeInMilliSeconds: Long): Calendar {
    val date = Date(timeInMilliSeconds)
    var calendar = Calendar.getInstance()
    calendar.time = date
    return calendar
}

fun getCalendarMonth(calendar: Calendar): Int {
    return calendar.get(Calendar.MONTH)
}

fun getCalendarYear(calendar: Calendar): Int {
    return calendar.get(Calendar.YEAR)
}

fun getNewCalendarReplica(calendar: Calendar): Calendar {
    var newCalendarObject = Calendar.getInstance()
    newCalendarObject.timeInMillis = calendar.timeInMillis
    return newCalendarObject
}

fun getNewCalendarReplica(timeInMilliSeconds: Long): Calendar {
    var newCalendarObject = Calendar.getInstance()
    newCalendarObject.timeInMillis = timeInMilliSeconds
    return newCalendarObject
}

inline fun Calendar.getNewCalendarReplica(applyFunction: (calendar: Calendar) -> Unit): Calendar{
    var newCalendarObject = getNewCalendarReplica(this)
    applyFunction(newCalendarObject)
    return newCalendarObject
}

fun Long.getCalendarFromTimeInMilli(): Calendar{
    var calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return calendar
}
