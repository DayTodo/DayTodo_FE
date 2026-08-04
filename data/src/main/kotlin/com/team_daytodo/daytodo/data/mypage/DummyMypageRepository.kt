package com.team_daytodo.daytodo.data.mypage

import com.team_daytodo.daytodo.domain.mypage.model.MypageProfile
import com.team_daytodo.daytodo.domain.mypage.repository.MypageRepository
import javax.inject.Inject
import kotlinx.coroutines.delay

class DummyMypageRepository @Inject constructor() : MypageRepository {
    private var notificationEnabled = false
    private var phoneNumber = "010-1234-5678"

    override suspend fun getProfile(): Result<MypageProfile> = runCatching {
        delay(MypageRequestDelayMillis)

        MypageProfile(
            name = "홍길동",
            nickname = "데이투두",
            email = "daytodo@example.com",
            phoneNumber = phoneNumber,
            linkedAccountProvider = "네이버",
            linkedAccountId = "daytodo@naver.com",
            notificationEnabled = notificationEnabled,
        )
    }

    override suspend fun setNotificationEnabled(enabled: Boolean): Result<Unit> = runCatching {
        delay(MypageRequestDelayMillis)

        notificationEnabled = enabled
    }

    override suspend fun requestPhoneVerificationCode(phoneNumber: String): Result<Unit> = runCatching {
        delay(MypageRequestDelayMillis)
    }

    // TODO: BE에 전화번호 변경 엔드포인트가 없음(UserController 확인 완료) — 더미로 로컬 상태만 갱신.
    override suspend fun changePhoneNumber(phoneNumber: String, verificationCode: String): Result<String> =
        runCatching {
            delay(MypageRequestDelayMillis)

            this.phoneNumber = phoneNumber
            phoneNumber
        }

    private companion object {
        const val MypageRequestDelayMillis = 300L
    }
}
