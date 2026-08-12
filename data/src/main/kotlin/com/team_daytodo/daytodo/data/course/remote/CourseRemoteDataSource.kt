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
import com.team_daytodo.daytodo.data.network.bodyOrThrow
import com.team_daytodo.daytodo.data.network.safeApiCall
import javax.inject.Inject
import kotlinx.serialization.json.Json

class CourseRemoteDataSource @Inject constructor(
    private val courseApi: CourseApi,
    private val json: Json,
) {
    suspend fun getCourses(): CoursesResponseDto =
        safeApiCall(json) {
            courseApi.getCourses().bodyOrThrow(json, GetCoursesEndpoint)
        }

    suspend fun createCourse(request: CourseCreateRequestDto): CourseCreateResponseDto =
        safeApiCall(json) {
            courseApi.createCourse(request).bodyOrThrow(json, CreateCourseEndpoint)
        }

    suspend fun joinCourse(request: CourseJoinRequestDto): CourseJoinResponseDto =
        safeApiCall(json) {
            courseApi.joinCourse(request).bodyOrThrow(json, JoinCourseEndpoint)
        }

    suspend fun getCourseMembers(courseId: Long): CourseMembersResponseDto =
        safeApiCall(json) {
            courseApi.getCourseMembers(courseId).bodyOrThrow(json, GetCourseMembersEndpoint)
        }

    suspend fun removeCourseMember(
        courseId: Long,
        memberId: Long,
    ): RemoveCourseMemberResponseDto =
        safeApiCall(json) {
            courseApi.removeCourseMember(courseId, memberId)
                .bodyOrThrow(json, RemoveCourseMemberEndpoint)
        }

    suspend fun getCoursePlaces(courseId: Long): CoursePlacesResponseDto =
        safeApiCall(json) {
            courseApi.getCoursePlaces(courseId).bodyOrThrow(json, GetCoursePlacesEndpoint)
        }

    suspend fun updateCourseSetting(
        courseId: Long,
        request: CourseSettingRequestDto,
    ): CourseSettingResponseDto =
        safeApiCall(json) {
            courseApi.updateCourseSetting(courseId, request)
                .bodyOrThrow(json, UpdateCourseSettingEndpoint)
        }

    suspend fun getCourseRecommendations(courseId: Long): CourseRecommendationsResponseDto =
        safeApiCall(json) {
            courseApi.getCourseRecommendations(courseId).bodyOrThrow(
                json = json,
                endpoint = GetCourseRecommendationsEndpoint,
            )
        }

    suspend fun recommendPlace(
        courseId: Long,
        request: PlaceRecommendationRequestDto,
    ): PlaceRecommendationResponseDto =
        safeApiCall(json) {
            courseApi.recommendPlace(courseId, request).bodyOrThrow(json, RecommendPlaceEndpoint)
        }

    suspend fun likeRecommendation(recommendationId: Long): PlaceRecommendationLikeResponseDto =
        safeApiCall(json) {
            courseApi.likeRecommendation(recommendationId)
                .bodyOrThrow(json, LikeRecommendationEndpoint)
        }

    suspend fun addRecommendationComment(
        recommendationId: Long,
        request: PlaceRecommendationCommentRequestDto,
    ): PlaceRecommendationCommentResponseDto =
        safeApiCall(json) {
            courseApi.addRecommendationComment(recommendationId, request)
                .bodyOrThrow(json, AddRecommendationCommentEndpoint)
        }

    suspend fun searchPlaces(query: String): PlacesSearchResponseDto =
        safeApiCall(json) {
            courseApi.searchPlaces(query).bodyOrThrow(json, SearchPlacesEndpoint)
        }

    suspend fun getAiCourseRecommendations(
        request: AiCourseRecommendationsRequestDto,
    ): AiCourseRecommendationsResponseDto =
        safeApiCall(json) {
            courseApi.getAiCourseRecommendations(request)
                .bodyOrThrow(json, AiCourseRecommendationsEndpoint)
        }

    private companion object {
        const val GetCoursesEndpoint = "GET courses"
        const val CreateCourseEndpoint = "POST courses"
        const val JoinCourseEndpoint = "POST courses/join"
        const val GetCourseMembersEndpoint = "GET courses/{courseId}/members"
        const val RemoveCourseMemberEndpoint = "DELETE courses/{courseId}/members/{memberId}"
        const val GetCoursePlacesEndpoint = "GET courses/{courseId}/places"
        const val UpdateCourseSettingEndpoint = "PATCH courses/{courseId}/setting"
        const val GetCourseRecommendationsEndpoint = "GET courses/{courseId}/recommendations"
        const val RecommendPlaceEndpoint = "POST courses/{courseId}/recommendations"
        const val LikeRecommendationEndpoint = "POST courses/recommendations/{recommendationId}/likes"
        const val AddRecommendationCommentEndpoint = "POST courses/recommendations/{recommendationId}/comments"
        const val SearchPlacesEndpoint = "GET places/search"
        const val AiCourseRecommendationsEndpoint = "POST courses/ai-recommendations"
    }
}
