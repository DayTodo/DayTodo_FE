package com.team_daytodo.daytodo.domain.mypage.usecase

import com.team_daytodo.daytodo.domain.mypage.model.Policies
import com.team_daytodo.daytodo.domain.mypage.repository.MypageRepository
import javax.inject.Inject

class GetPoliciesUseCase @Inject constructor(
    private val mypageRepository: MypageRepository,
) {
    suspend operator fun invoke(): Result<Policies> = mypageRepository.getPolicies()
}
