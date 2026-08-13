package com.team_daytodo.daytodo.data.home

import com.team_daytodo.daytodo.core.model.CommonError
import com.team_daytodo.daytodo.data.api.HomeApi
import com.team_daytodo.daytodo.data.dto.home.toDomain
import com.team_daytodo.daytodo.data.network.safeApiResult
import com.team_daytodo.daytodo.domain.home.model.HomeCourses
import com.team_daytodo.daytodo.domain.home.model.HomeException
import com.team_daytodo.daytodo.domain.home.model.HomeLoadException
import com.team_daytodo.daytodo.domain.home.model.HomeMagazine
import com.team_daytodo.daytodo.domain.home.model.HomeUnauthorizedException
import com.team_daytodo.daytodo.domain.home.repository.HomeRepository
import javax.inject.Inject
import kotlinx.serialization.json.Json
import retrofit2.HttpException

class HomeRepositoryImpl @Inject constructor(
    private val homeApi: HomeApi,
    private val json: Json,
) : HomeRepository {
    override suspend fun getCourses(): Result<HomeCourses> =
        safeApiResult(json) {
            homeApi.getCourses()
        }.mapCatching {
            it.toDomain()
        }.recoverCatching { cause -> throw cause.toHomeException() }

    override suspend fun getTodayPickMagazines(): Result<List<HomeMagazine>> =
        safeApiResult(json) {
            homeApi.getPlacesMagazines()
        }.mapCatching {
            it.toDomain()
        }.recoverCatching { cause -> throw cause.toHomeException() }
}

private fun Throwable.toHomeException(): Throwable = when {
    this is HomeException -> this
    this is CommonError -> when (this) {
        is CommonError.Unauthorized -> HomeUnauthorizedException(this)
        else -> this
    }
    this is HttpException && code() == 401 -> HomeUnauthorizedException(this)
    else -> HomeLoadException(this)
}
