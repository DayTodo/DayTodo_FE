package com.team_daytodo.daytodo.domain.mypage.repository

import com.team_daytodo.daytodo.domain.mypage.model.MypageProfile

interface MypageRepository {
    suspend fun getProfile(): Result<MypageProfile>

    suspend fun setNotificationEnabled(enabled: Boolean): Result<Unit>
}
