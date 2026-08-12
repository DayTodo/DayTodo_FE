package com.team_daytodo.daytodo.data.home

import com.team_daytodo.daytodo.data.api.HomeApi
import com.team_daytodo.daytodo.data.dto.home.toDomain
import com.team_daytodo.daytodo.domain.home.model.HomeCourses
import com.team_daytodo.daytodo.domain.home.model.HomeException
import com.team_daytodo.daytodo.domain.home.model.HomeLoadException
import com.team_daytodo.daytodo.domain.home.model.HomeMagazine
import com.team_daytodo.daytodo.domain.home.model.HomeUnauthorizedException
import com.team_daytodo.daytodo.domain.home.repository.HomeRepository
import javax.inject.Inject
import retrofit2.HttpException

class HomeRepositoryImpl @Inject constructor(
    private val homeApi: HomeApi,
) : HomeRepository {
    override suspend fun getCourses(): Result<HomeCourses> = runCatching {
        homeApi.getCourses().toDomain()
    }.recoverCatching { cause -> throw cause.toHomeException() }

    override suspend fun getTodayPickMagazines(): Result<List<HomeMagazine>> = runCatching {
        homeApi.getPlacesMagazines().toDomain()
    }.recoverCatching { cause -> throw cause.toHomeException() }
}

private fun Throwable.toHomeException(): HomeException = when {
    this is HomeException -> this
    this is HttpException && code() == 401 -> HomeUnauthorizedException(this)
    else -> HomeLoadException(this)
}
