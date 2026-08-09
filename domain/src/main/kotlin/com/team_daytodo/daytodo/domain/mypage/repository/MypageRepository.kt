package com.team_daytodo.daytodo.domain.mypage.repository

import com.team_daytodo.daytodo.domain.mypage.model.InterestRegion
import com.team_daytodo.daytodo.domain.mypage.model.MypageProfile
import com.team_daytodo.daytodo.domain.mypage.model.Policies

interface MypageRepository {
    suspend fun getProfile(): Result<MypageProfile>

    suspend fun updateProfile(nickname: String, profileImageUri: String?): Result<MypageProfile>

    suspend fun getNotificationSettings(): Result<Boolean>

    suspend fun setNotificationEnabled(enabled: Boolean): Result<Unit>

    suspend fun getInterestRegions(): Result<List<InterestRegion>>

    suspend fun updateInterestRegions(regionIds: List<Long>): Result<List<InterestRegion>>

    suspend fun getPolicies(): Result<Policies>

    suspend fun withdraw(): Result<Unit>

    suspend fun sendFeedback(content: String): Result<Unit>

    suspend fun logout(refreshToken: String): Result<Unit>

    suspend fun registerFcmToken(token: String): Result<Unit>

    suspend fun deleteFcmToken(token: String): Result<Unit>
}
