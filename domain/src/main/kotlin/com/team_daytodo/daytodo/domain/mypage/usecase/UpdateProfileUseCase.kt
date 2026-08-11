package com.team_daytodo.daytodo.domain.mypage.usecase

import com.team_daytodo.daytodo.domain.mypage.model.MypageProfile
import com.team_daytodo.daytodo.domain.mypage.repository.MypageRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val mypageRepository: MypageRepository,
) {
    suspend operator fun invoke(nickname: String, profileImageUri: String?): Result<MypageProfile> =
        mypageRepository.updateProfile(nickname, profileImageUri)
}
