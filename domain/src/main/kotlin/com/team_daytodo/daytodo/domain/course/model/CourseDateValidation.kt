package com.team_daytodo.daytodo.domain.course.model

internal fun CourseDate.isValid(): Boolean {
    if (year < 1 || month !in 1..12) return false

    return day in 1..daysInMonth(year, month)
}

private fun daysInMonth(year: Int, month: Int): Int =
    when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> 0
    }

private fun isLeapYear(year: Int): Boolean =
    year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
