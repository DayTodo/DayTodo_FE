package com.team_daytodo.daytodo.domain.mypage.usecase

import com.team_daytodo.daytodo.domain.mypage.repository.MypageRepository
import javax.inject.Inject

class ChangePhoneNumberUseCase @Inject constructor(
    private val mypageRepository: MypageRepository,
) {
    suspend operator fun invoke(phoneNumber: String, verificationCode: String): Result<String> =
        mypageRepository.changePhoneNumber(phoneNumber, verificationCode)
}
