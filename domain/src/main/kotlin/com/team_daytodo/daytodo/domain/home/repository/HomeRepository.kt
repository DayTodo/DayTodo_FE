package com.team_daytodo.daytodo.domain.home.repository

import com.team_daytodo.daytodo.domain.home.model.HomeCourses
import com.team_daytodo.daytodo.domain.home.model.HomeMagazine

interface HomeRepository {
    suspend fun getCourses(): Result<HomeCourses>

    suspend fun getTodayPickMagazines(): Result<List<HomeMagazine>>
}
