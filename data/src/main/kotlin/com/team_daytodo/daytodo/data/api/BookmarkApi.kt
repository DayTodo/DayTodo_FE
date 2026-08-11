package com.team_daytodo.daytodo.data.api

import com.team_daytodo.daytodo.data.dto.bookmark.CreateBookmarkRequest
import com.team_daytodo.daytodo.data.dto.bookmark.CreateBookmarkResponse
import com.team_daytodo.daytodo.data.dto.bookmark.GetBookmarksResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BookmarkApi {
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
