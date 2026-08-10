package com.team_daytodo.daytodo.data.api

import com.team_daytodo.daytodo.data.dto.region.GetRegionsResponse
import retrofit2.http.GET

interface RegionApi {
    @GET("regions")
    suspend fun getRegions(): GetRegionsResponse
}
