package com.team_daytodo.daytodo.data.calendar

import com.team_daytodo.daytodo.data.api.CalendarApi
import com.team_daytodo.daytodo.data.dto.calendar.toDomain
import com.team_daytodo.daytodo.data.network.safeApiResult
import com.team_daytodo.daytodo.domain.calendar.model.CalendarDate
import com.team_daytodo.daytodo.domain.calendar.repository.CalendarRepository
import javax.inject.Inject
import kotlinx.serialization.json.Json

class CalendarRepositoryImpl @Inject constructor(
    private val calendarApi: CalendarApi,
    private val json: Json,
) : CalendarRepository {
    override suspend fun getCalendar(year: Int, month: Int): Result<List<CalendarDate>> =
        safeApiResult(json) {
            calendarApi.getCalendar(year, month)
        }.mapCatching {
            it.toDomain()
        }
}
