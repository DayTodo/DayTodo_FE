package com.team_daytodo.daytodo.domain.today.repository

import com.team_daytodo.daytodo.domain.today.model.TodayCourse

interface TodayRepository {
    suspend fun getTodayCourse(): Result<TodayCourse>
}
