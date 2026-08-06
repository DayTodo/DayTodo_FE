package com.team_daytodo.daytodo.data.mypage

import com.team_daytodo.daytodo.data.api.MypageApi
import com.team_daytodo.daytodo.data.dto.mypage.LogoutRequest
import com.team_daytodo.daytodo.data.dto.mypage.SendFeedbackRequest
import com.team_daytodo.daytodo.data.dto.mypage.UpdateInterestRegionsRequest
import com.team_daytodo.daytodo.data.dto.mypage.toDomain
import com.team_daytodo.daytodo.domain.mypage.model.InterestRegion
import com.team_daytodo.daytodo.domain.mypage.model.MypageProfile
import com.team_daytodo.daytodo.domain.mypage.model.Policies
import com.team_daytodo.daytodo.domain.mypage.repository.MypageRepository
import java.io.File
import javax.inject.Inject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import retrofit2.Response

class MypageRepositoryImpl @Inject constructor(
    private val mypageApi: MypageApi,
) : MypageRepository {
    private var notificationEnabled = false

    override suspend fun getProfile(): Result<MypageProfile> = runCatching {
        mypageApi.getProfile().toDomain()
    }

    override suspend fun updateProfile(nickname: String, profileImage: File?): Result<MypageProfile> = runCatching {
        val nicknamePart = nickname.toRequestBody(TextMediaType)
        val imagePart = profileImage?.let { file ->
            MultipartBody.Part.createFormData(
                name = "profileImage",
                filename = file.name,
                body = file.asRequestBody(ImageMediaType),
            )
        }
        mypageApi.updateProfile(nicknamePart, imagePart).toDomain()
    }

    override suspend fun setNotificationEnabled(enabled: Boolean): Result<Unit> = runCatching {
        notificationEnabled = enabled
    }

    override suspend fun requestPhoneVerificationCode(phoneNumber: String): Result<Unit> = runCatching { }

    override suspend fun changePhoneNumber(phoneNumber: String, verificationCode: String): Result<String> =
        runCatching { phoneNumber }

    override suspend fun getInterestRegions(): Result<List<InterestRegion>> = runCatching {
        mypageApi.getInterestRegions().toDomain()
    }

    override suspend fun updateInterestRegions(regionIds: List<Long>): Result<List<InterestRegion>> = runCatching {
        mypageApi.updateInterestRegions(UpdateInterestRegionsRequest(regionIds)).toDomain()
    }

    override suspend fun getPolicies(): Result<Policies> = runCatching {
        mypageApi.getPolicies().toDomain()
    }

    override suspend fun withdraw(): Result<Unit> = runCatching {
        mypageApi.withdraw().throwIfNotSuccessful()
    }

    override suspend fun sendFeedback(content: String): Result<Unit> = runCatching {
        mypageApi.sendFeedback(SendFeedbackRequest(content)).throwIfNotSuccessful()
    }

    override suspend fun logout(refreshToken: String): Result<Unit> = runCatching {
        mypageApi.logout(LogoutRequest(refreshToken)).throwIfNotSuccessful()
    }

    private fun Response<Unit>.throwIfNotSuccessful() {
        if (!isSuccessful) throw HttpException(this)
    }

    private companion object {
        val TextMediaType = "text/plain".toMediaTypeOrNull()
        val ImageMediaType = "image/*".toMediaTypeOrNull()
    }
}
