package com.pq.shiftmadeeasy.util

import java.util.*

fun getCalendarDateFromMilliseconds(timeInMilliSeconds: Long): Calendar {
    val date = Date(timeInMilliSeconds)
    var calendar = Calendar.getInstance()
    calendar.time = date
    return calendar
}

fun  isMonthAlreadySet(listOfMonthAlreadySet: List<Calendar>, currentMonthCalendar: Calendar): Boolean{
    val currentCalendarMonth = getCalendarMonth(currentMonthCalendar)
    val currentCalendarYear = getCalendarYear(currentMonthCalendar)
    listOfMonthAlreadySet.forEach {
        val calendarMonthOfListItem = getCalendarMonth(it)
        if(calendarMonthOfListItem == currentCalendarMonth){
            val calendarYearOfListItem = getCalendarYear(it)
            if(calendarYearOfListItem == currentCalendarYear)
                return true
        }
    }
    return false
}

fun getCalendarMonth(calendar: Calendar): Int {
    return calendar.get(Calendar.MONTH)
}

fun getCalendarYear(calendar: Calendar):Int{
    return calendar.get(Calendar.YEAR)
}

fun getSimilarCalendarObject(rhsCalendarObject: Calendar): Calendar{
    var calendar = Calendar.getInstance()
    calendar.timeInMillis = rhsCalendarObject.timeInMillis
    return calendar
}
