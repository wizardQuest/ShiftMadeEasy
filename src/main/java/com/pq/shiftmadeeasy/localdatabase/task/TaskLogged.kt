package com.pq.focuslist.localdatabase.task

import java.sql.Timestamp

data class TaskLogged(
    val startTime: Timestamp,
    val endTime: Timestamp,
    val qualityOfTheSession: QualityOfTheSession
)

enum class QualityOfTheSession(val stars: Int) {
    WORSE(1),
    BAD(2),
    AVERAGE(3),
    GOOD(4),
    EXCELLENT(5)
}