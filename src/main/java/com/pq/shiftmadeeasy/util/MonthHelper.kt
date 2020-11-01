package com.pq.shiftmadeeasy.util

fun getMonthByName(monthInNumber: Int): String {
    return when (monthInNumber) {
        0 -> "Jan"
        1 -> "Feb"
        2 -> "Mar"
        3 -> "Apr"
        4 -> "May"
        5 -> "Jun"
        6 -> "Jul"
        7 -> "Aug"
        8 -> "Sep"
        9 -> "Oct"
        10 -> "Num"
        11 -> "Dec"
        else -> ""
    }
}

fun getWeekByName(dayOfWeek: String): String {
    return when (dayOfWeek) {
        "Monday" -> "Mon"
        "Tuesday" -> "Tue"
        "Wednesday" -> "Wed"
        "Thursday" -> "Thu"
        "Friday" -> "Fri"
        "Saturday" -> "Sat"
        "Sunday" -> "Sun"
        else -> ""
    }
}