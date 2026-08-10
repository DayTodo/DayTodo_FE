package com.team_daytodo.daytodo.domain.mypage.usecase

import com.team_daytodo.daytodo.domain.mypage.repository.MypageRepository
import javax.inject.Inject

class RequestPhoneVerificationCodeUseCase @Inject constructor(
    private val mypageRepository: MypageRepository,
) {
    suspend operator fun invoke(phoneNumber: String): Result<Unit> =
        mypageRepository.requestPhoneVerificationCode(phoneNumber)
}
