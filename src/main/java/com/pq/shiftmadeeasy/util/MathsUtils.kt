package com.pq.shiftmadeeasy.util

fun getTimeAsDay(timeInMilliSeconds: Long): Long {
    return timeInMilliSeconds/86400000
}

fun getNextDay(timeInMilliSeconds: Long): Long {
    return timeInMilliSeconds + 86400000
}