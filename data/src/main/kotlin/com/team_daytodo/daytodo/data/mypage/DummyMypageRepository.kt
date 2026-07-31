package com.team_daytodo.daytodo.data.mypage

import com.team_daytodo.daytodo.domain.mypage.model.MypageProfile
import com.team_daytodo.daytodo.domain.mypage.repository.MypageRepository
import javax.inject.Inject
import kotlinx.coroutines.delay

class DummyMypageRepository @Inject constructor() : MypageRepository {
    private var notificationEnabled = false

    override suspend fun getProfile(): Result<MypageProfile> = runCatching {
        delay(MypageRequestDelayMillis)

        MypageProfile(
            name = "홍길동",
            nickname = "데이투두",
            email = "daytodo@example.com",
            phoneNumber = "000-0000-0000",
            linkedAccountProvider = "네이버",
            linkedAccountId = "daytodo@naver.com",
            notificationEnabled = notificationEnabled,
        )
    }

    override suspend fun setNotificationEnabled(enabled: Boolean): Result<Unit> = runCatching {
        delay(MypageRequestDelayMillis)

        notificationEnabled = enabled
    }

    private companion object {
        const val MypageRequestDelayMillis = 300L
    }
}
