package com.team_daytodo.daytodo.domain.mypage.usecase

import com.team_daytodo.daytodo.domain.mypage.repository.MypageRepository
import javax.inject.Inject

class SetNotificationEnabledUseCase @Inject constructor(
    private val mypageRepository: MypageRepository,
) {
    suspend operator fun invoke(enabled: Boolean): Result<Unit> =
        mypageRepository.setNotificationEnabled(enabled)
}
