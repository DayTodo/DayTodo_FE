package com.team_daytodo.daytodo.data.api

import com.team_daytodo.daytodo.data.dto.home.GetCoursesResponse
import com.team_daytodo.daytodo.data.dto.home.GetPlacesMagazinesResponse
import retrofit2.http.GET

interface HomeApi {
    @GET("courses")
    suspend fun getCourses(): GetCoursesResponse

    @GET("places/magazines")
    suspend fun getPlacesMagazines(): GetPlacesMagazinesResponse
}
