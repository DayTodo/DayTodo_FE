package com.team_daytodo.daytodo.data.api

import com.team_daytodo.daytodo.data.dto.mypage.InterestRegionsResponse
import com.team_daytodo.daytodo.data.dto.mypage.LogoutRequest
import com.team_daytodo.daytodo.data.dto.mypage.PoliciesResponse
import com.team_daytodo.daytodo.data.dto.mypage.ProfileResponse
import com.team_daytodo.daytodo.data.dto.mypage.SendFeedbackRequest
import com.team_daytodo.daytodo.data.dto.mypage.UpdateInterestRegionsRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

interface MypageApi {
    @GET("users/profile")
    suspend fun getProfile(): ProfileResponse

    @Multipart
    @PATCH("users/profile")
    suspend fun updateProfile(
        @Part("nickname") nickname: RequestBody,
        @Part profileImage: MultipartBody.Part?,
    ): ProfileResponse

    @GET("users/interest-region")
    suspend fun getInterestRegions(): InterestRegionsResponse

    @PATCH("users/interest-regions")
    suspend fun updateInterestRegions(@Body request: UpdateInterestRegionsRequest): InterestRegionsResponse

    @GET("users/policies")
    suspend fun getPolicies(): PoliciesResponse

    @DELETE("users/me")
    suspend fun withdraw(): Response<Unit>

    @POST("users/feedback")
    suspend fun sendFeedback(@Body request: SendFeedbackRequest): Response<Unit>

    @POST("auth/logout")
    suspend fun logout(@Body request: LogoutRequest): Response<Unit>
}
