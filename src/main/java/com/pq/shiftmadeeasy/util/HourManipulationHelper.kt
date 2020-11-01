package com.pq.shiftmadeeasy.util

import com.pq.shiftmadeeasy.data.Time

fun getTimeDifference(fromTime: String, toTime: String): Time {
    val fromTimeHours = fromTime.split(":")[0].toInt()
    val fromTimeMinutes = fromTime.split(":")[1].split(" ")[0].toInt()

    val toTimeHours = toTime.split(":")[0].toInt()
    val toTimeMinutes = toTime.split(":")[1].split(" ")[0].toInt()

    var fromTimeInMinutes = (fromTimeHours * 60) + fromTimeMinutes
    var toTimeInMinutes = (toTimeHours * 60) + toTimeMinutes

    var resultantHours = 0
    var resultantMinutes = 0

    if (fromTimeInMinutes < toTimeInMinutes) {
        val resultantTime = toTimeInMinutes - fromTimeInMinutes
        resultantHours = resultantTime / 60
        resultantMinutes = resultantTime % 60
    } else if (fromTimeInMinutes > toTimeInMinutes) {
        val resultantTime = (24 * 60) - fromTimeInMinutes + toTimeInMinutes
        resultantHours = resultantTime / 60
        resultantMinutes = resultantTime % 60
    }
    return Time(resultantHours, resultantMinutes)
}

fun manipulateTimeToDisplay(time: Time): String {
    val hour = String.format("%02d", time.hours)
    val min = String.format("%02d", time.minutes)
    return "$hour:$min Hrs"
}

fun getCumulativeHours(totalTime: String?, taskLoggedTime: String): String {
    val totalTimeHours = totalTime?.split(":")?.get(0)?.toInt()
    val totalTimeMinutes = totalTime?.split(":")?.get(1)?.split(" ")?.get(0)?.toInt()

    val taskLoggedHours = taskLoggedTime?.split(":")?.get(0)?.toInt()
    val taskLoggedMinutes = taskLoggedTime?.split(":")?.get(1)?.split(" ")[0].toInt()

    var resultantMinutes: Int = totalTimeMinutes?.plus(taskLoggedMinutes)?.rem(60) ?: 0
    var resultantHour = totalTimeHours?.let { taskLoggedHours?.plus(it) }?.plus(
        (totalTimeMinutes?.plus(taskLoggedMinutes)
            ?.div(60)!!)
    ) ?: 0

    return manipulateTimeToDisplay(Time(resultantHour, resultantMinutes))
}