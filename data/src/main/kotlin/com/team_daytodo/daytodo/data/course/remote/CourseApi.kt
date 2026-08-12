package com.team_daytodo.daytodo.data.course.remote

import com.team_daytodo.daytodo.data.course.remote.dto.AiCourseRecommendationsRequestDto
import com.team_daytodo.daytodo.data.course.remote.dto.AiCourseRecommendationsResponseDto
import com.team_daytodo.daytodo.data.course.remote.dto.CourseCreateRequestDto
import com.team_daytodo.daytodo.data.course.remote.dto.CourseCreateResponseDto
import com.team_daytodo.daytodo.data.course.remote.dto.CourseJoinRequestDto
import com.team_daytodo.daytodo.data.course.remote.dto.CourseJoinResponseDto
import com.team_daytodo.daytodo.data.course.remote.dto.CourseMembersResponseDto
import com.team_daytodo.daytodo.data.course.remote.dto.CoursePlacesResponseDto
import com.team_daytodo.daytodo.data.course.remote.dto.CourseRecommendationsResponseDto
import com.team_daytodo.daytodo.data.course.remote.dto.CourseSettingRequestDto
import com.team_daytodo.daytodo.data.course.remote.dto.CourseSettingResponseDto
import com.team_daytodo.daytodo.data.course.remote.dto.CoursesResponseDto
import com.team_daytodo.daytodo.data.course.remote.dto.PlaceRecommendationCommentRequestDto
import com.team_daytodo.daytodo.data.course.remote.dto.PlaceRecommendationCommentResponseDto
import com.team_daytodo.daytodo.data.course.remote.dto.PlaceRecommendationLikeResponseDto
import com.team_daytodo.daytodo.data.course.remote.dto.PlaceRecommendationRequestDto
import com.team_daytodo.daytodo.data.course.remote.dto.PlaceRecommendationResponseDto
import com.team_daytodo.daytodo.data.course.remote.dto.PlacesSearchResponseDto
import com.team_daytodo.daytodo.data.course.remote.dto.RemoveCourseMemberResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CourseApi {
    @GET("courses")
    suspend fun getCourses(
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
    ): Response<CoursesResponseDto>

    @POST("courses")
    suspend fun createCourse(
        @Body request: CourseCreateRequestDto,
    ): Response<CourseCreateResponseDto>

    @POST("courses/join")
    suspend fun joinCourse(
        @Body request: CourseJoinRequestDto,
    ): Response<CourseJoinResponseDto>

    @GET("courses/{courseId}/members")
    suspend fun getCourseMembers(
        @Path("courseId") courseId: Long,
    ): Response<CourseMembersResponseDto>

    @DELETE("courses/{courseId}/members/{memberId}")
    suspend fun removeCourseMember(
        @Path("courseId") courseId: Long,
        @Path("memberId") memberId: Long,
    ): Response<RemoveCourseMemberResponseDto>

    @GET("courses/{courseId}/places")
    suspend fun getCoursePlaces(
        @Path("courseId") courseId: Long,
    ): Response<CoursePlacesResponseDto>

    @PATCH("courses/{courseId}/setting")
    suspend fun updateCourseSetting(
        @Path("courseId") courseId: Long,
        @Body request: CourseSettingRequestDto,
    ): Response<CourseSettingResponseDto>

    @GET("courses/{courseId}/recommendations")
    suspend fun getCourseRecommendations(
        @Path("courseId") courseId: Long,
        @Query("recommender") recommender: String? = null,
    ): Response<CourseRecommendationsResponseDto>

    @POST("courses/{courseId}/recommendations")
    suspend fun recommendPlace(
        @Path("courseId") courseId: Long,
        @Body request: PlaceRecommendationRequestDto,
    ): Response<PlaceRecommendationResponseDto>

    @POST("courses/recommendations/{recommendationId}/likes")
    suspend fun likeRecommendation(
        @Path("recommendationId") recommendationId: Long,
    ): Response<PlaceRecommendationLikeResponseDto>

    @POST("courses/recommendations/{recommendationId}/comments")
    suspend fun addRecommendationComment(
        @Path("recommendationId") recommendationId: Long,
        @Body request: PlaceRecommendationCommentRequestDto,
    ): Response<PlaceRecommendationCommentResponseDto>

    @GET("places/search")
    suspend fun searchPlaces(
        @Query("query") query: String,
    ): Response<PlacesSearchResponseDto>

    @POST("courses/ai-recommendations")
    suspend fun getAiCourseRecommendations(
        @Body request: AiCourseRecommendationsRequestDto,
    ): Response<AiCourseRecommendationsResponseDto>
}
