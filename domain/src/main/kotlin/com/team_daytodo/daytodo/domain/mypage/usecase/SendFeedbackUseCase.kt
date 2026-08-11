package com.team_daytodo.daytodo.domain.mypage.usecase

import com.team_daytodo.daytodo.domain.mypage.repository.MypageRepository
import javax.inject.Inject

class SendFeedbackUseCase @Inject constructor(
    private val mypageRepository: MypageRepository,
) {
    suspend operator fun invoke(content: String): Result<Unit> = mypageRepository.sendFeedback(content)
}
