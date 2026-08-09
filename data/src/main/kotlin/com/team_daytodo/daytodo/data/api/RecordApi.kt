package com.team_daytodo.daytodo.data.api

import com.team_daytodo.daytodo.data.dto.record.CoursePlaceDto
import com.team_daytodo.daytodo.data.dto.record.MemoryByDateDto
import com.team_daytodo.daytodo.data.dto.record.PhotosDto
import com.team_daytodo.daytodo.data.dto.record.WriteDiaryRequest
import com.team_daytodo.daytodo.data.dto.record.WriteDiaryResponse
import java.time.LocalDate
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RecordApi {
    @GET("courses/{courseId}/places")
    suspend fun getCoursePlaces(@Path("courseId") courseId: Long): List<CoursePlaceDto>

    @GET("courses/{courseId}/memory-photos")
    suspend fun getMemoryPhotos(@Path("courseId") courseId: Long): PhotosDto

    @GET("courses/diaries")
    suspend fun getMemoryByDate(@Query("date") date: LocalDate): MemoryByDateDto

    @POST("courses/diaries")
    suspend fun writeDiary(@Body request: WriteDiaryRequest): WriteDiaryResponse
}
