package com.team_daytodo.daytodo.data.api

import com.team_daytodo.daytodo.data.dto.today.CompleteCourseResponse
import com.team_daytodo.daytodo.data.dto.today.GetCoursePlacesResponse
import com.team_daytodo.daytodo.data.dto.today.GetTodayCourseResponse
import com.team_daytodo.daytodo.data.dto.today.ReorderCoursePlacesRequest
import com.team_daytodo.daytodo.data.dto.today.SaveMemoryPhotosRequest
import com.team_daytodo.daytodo.data.dto.today.SaveMemoryPhotosResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface TodayApi {
    @GET("courses/today")
    suspend fun getTodayCourse(): GetTodayCourseResponse

    @POST("courses/{courseId}/complete")
    suspend fun completeCourse(@Path("courseId") courseId: Long): CompleteCourseResponse

    @POST("courses/{courseId}/photos")
    suspend fun saveMemoryPhotos(
        @Path("courseId") courseId: Long,
        @Body request: SaveMemoryPhotosRequest,
    ): SaveMemoryPhotosResponse

    @PATCH("courses/{courseId}/today-places/order")
    suspend fun reorderCoursePlaces(
        @Path("courseId") courseId: Long,
        @Body request: ReorderCoursePlacesRequest,
    ): GetCoursePlacesResponse

    @DELETE("courses/{courseId}/today-places/{coursePlaceId}")
    suspend fun deleteCoursePlace(
        @Path("courseId") courseId: Long,
        @Path("coursePlaceId") coursePlaceId: Long,
    ): GetCoursePlacesResponse
}
