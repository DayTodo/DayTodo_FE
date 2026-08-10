package com.team_daytodo.daytodo.domain.mypage.usecase

import com.team_daytodo.daytodo.domain.mypage.repository.MypageRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val mypageRepository: MypageRepository,
) {
    suspend operator fun invoke(refreshToken: String): Result<Unit> = mypageRepository.logout(refreshToken)
}
