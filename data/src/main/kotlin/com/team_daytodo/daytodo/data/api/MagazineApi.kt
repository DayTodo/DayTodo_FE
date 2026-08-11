package com.team_daytodo.daytodo.data.api

import com.team_daytodo.daytodo.data.dto.magazine.CreateBookmarkRequest
import com.team_daytodo.daytodo.data.dto.magazine.CreateBookmarkResponse
import com.team_daytodo.daytodo.data.dto.magazine.GetBookmarksResponse
import com.team_daytodo.daytodo.data.dto.magazine.GetMagazineDetailResponse
import com.team_daytodo.daytodo.data.dto.magazine.GetMagazinesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MagazineApi {
    @GET("places/magazines")
    suspend fun getMagazines(): GetMagazinesResponse

    @GET("places/magazines/{magazineId}")
    suspend fun getMagazineDetail(@Path("magazineId") magazineId: Long): GetMagazineDetailResponse

    @GET("places/bookmarks")
    suspend fun getBookmarks(
        @Query("sort") sort: String?,
        @Query("regionId") regionId: Long?,
    ): GetBookmarksResponse

    @POST("places/bookmarks")
    suspend fun createBookmark(@Body request: CreateBookmarkRequest): CreateBookmarkResponse

    @DELETE("places/bookmarks/{bookmarkId}")
    suspend fun deleteBookmark(@Path("bookmarkId") bookmarkId: Long): Response<Unit>
}
