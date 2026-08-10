package com.team_daytodo.daytodo.data.api

import com.team_daytodo.daytodo.data.dto.calendar.CalendarResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CalendarApi {
    @GET("courses/calendar")
    suspend fun getCalendar(
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): CalendarResponseDto
}
