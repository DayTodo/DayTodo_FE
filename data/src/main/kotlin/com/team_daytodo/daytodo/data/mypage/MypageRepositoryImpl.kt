package com.team_daytodo.daytodo.data.mypage

import android.content.Context
import android.net.Uri
import com.team_daytodo.daytodo.core.model.CommonError
import com.team_daytodo.daytodo.data.api.MypageApi
import com.team_daytodo.daytodo.data.auth.local.SignupEmailLocalDataSource
import com.team_daytodo.daytodo.data.dto.mypage.ChangePasswordRequest
import com.team_daytodo.daytodo.data.dto.mypage.DeleteFcmTokenRequest
import com.team_daytodo.daytodo.data.dto.mypage.LogoutRequest
import com.team_daytodo.daytodo.data.dto.mypage.RegisterFcmTokenRequest
import com.team_daytodo.daytodo.data.dto.mypage.SendFeedbackRequest
import com.team_daytodo.daytodo.data.dto.mypage.UpdateInterestRegionsRequest
import com.team_daytodo.daytodo.data.dto.mypage.UpdateNotificationSettingsRequest
import com.team_daytodo.daytodo.data.dto.mypage.toDomain
import com.team_daytodo.daytodo.data.network.safeApiResult
import com.team_daytodo.daytodo.data.network.successOrThrow
import com.team_daytodo.daytodo.domain.mypage.model.ChangePasswordFailedException
import com.team_daytodo.daytodo.domain.mypage.model.FeedbackSubmitFailedException
import com.team_daytodo.daytodo.domain.mypage.model.FeedbackTooShortException
import com.team_daytodo.daytodo.domain.mypage.model.FeedbackUnauthorizedException
import com.team_daytodo.daytodo.domain.mypage.model.InterestRegion
import com.team_daytodo.daytodo.domain.mypage.model.InvalidCurrentPasswordException
import com.team_daytodo.daytodo.domain.mypage.model.MypageProfile
import com.team_daytodo.daytodo.domain.mypage.model.Policies
import com.team_daytodo.daytodo.domain.mypage.model.SocialAccountPasswordChangeNotAllowedException
import com.team_daytodo.daytodo.domain.mypage.repository.MypageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class MypageRepositoryImpl @Inject constructor(
    private val mypageApi: MypageApi,
    private val signupEmailLocalDataSource: SignupEmailLocalDataSource,
    private val json: Json,
    @param:ApplicationContext private val context: Context,
) : MypageRepository {
    override suspend fun getProfile(): Result<MypageProfile> =
        safeApiResult(json) {
            mypageApi.getProfile()
        }.mapCatching {
            it.toDomain().copy(email = signupEmailLocalDataSource.getEmail())
        }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun updateProfile(nickname: String, profileImageUri: String?): Result<MypageProfile> =
        try {
            val nicknamePart = nickname.toRequestBody(TextMediaType)
            val imagePart = profileImageUri?.let { uri -> createImagePart(uri) }
            val response = safeApiResult(json) {
                mypageApi.updateProfile(nicknamePart, imagePart)
            }.getOrThrow()
            Result.success(
                response.toDomain().copy(email = signupEmailLocalDataSource.getEmail()),
            )
        } catch (cause: CancellationException) {
            throw cause
        } catch (cause: Throwable) {
            Result.failure(cause)
        }

    // updateProfile은 BE가 URL이 아닌 raw multipart 파일을 직접 받으므로(오늘 화면의
    // imageUrls 방식과 다름), 갤러리에서 고른 content:// Uri를 캐시 파일로 복사해 업로드한다.
    private fun createImagePart(uriString: String): MultipartBody.Part {
        val file = File(context.cacheDir, "profile_${System.currentTimeMillis()}.jpg")
        context.contentResolver.openInputStream(Uri.parse(uriString))?.use { input ->
            file.outputStream().use { output -> input.copyTo(output) }
        }
        return MultipartBody.Part.createFormData(
            name = "profileImage",
            filename = file.name,
            body = file.asRequestBody(ImageMediaType),
        )
    }

    override suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> =
        safeApiResult(json) {
            mypageApi.changePassword(ChangePasswordRequest(currentPassword, newPassword)).successOrThrow(json)
        }.recoverCatching { cause -> throw cause.toChangePasswordException() }

    override suspend fun getNotificationSettings(): Result<Boolean> =
        safeApiResult(json) {
            mypageApi.getNotificationSettings()
        }.mapCatching {
            it.pushEnabled
        }

    override suspend fun setNotificationEnabled(enabled: Boolean): Result<Unit> = safeApiResult(json) {
        mypageApi.updateNotificationSettings(UpdateNotificationSettingsRequest(enabled))
        Unit
    }

    override suspend fun getInterestRegions(): Result<List<InterestRegion>> =
        safeApiResult(json) {
            mypageApi.getInterestRegions()
        }.mapCatching {
            it.toDomain()
        }

    override suspend fun updateInterestRegions(regionIds: List<Long>): Result<List<InterestRegion>> =
        safeApiResult(json) {
            mypageApi.updateInterestRegions(UpdateInterestRegionsRequest(regionIds))
        }.mapCatching {
            it.toDomain()
        }

    override suspend fun getPolicies(): Result<Policies> =
        safeApiResult(json) {
            mypageApi.getPolicies()
        }.mapCatching {
            it.toDomain()
        }

    override suspend fun withdraw(): Result<Unit> = safeApiResult(json) {
        mypageApi.withdraw().successOrThrow(json)
    }

    override suspend fun sendFeedback(content: String): Result<Unit> = safeApiResult(json) {
        mypageApi.sendFeedback(SendFeedbackRequest(content)).successOrThrow(json)
    }.recoverCatching { cause -> throw cause.toFeedbackException() }

    override suspend fun logout(refreshToken: String): Result<Unit> = safeApiResult(json) {
        mypageApi.logout(LogoutRequest(refreshToken)).successOrThrow(json)
    }

    override suspend fun registerFcmToken(token: String): Result<Unit> = safeApiResult(json) {
        mypageApi.registerFcmToken(RegisterFcmTokenRequest(token, FcmDevicePlatform)).successOrThrow(json)
    }

    override suspend fun deleteFcmToken(token: String): Result<Unit> = safeApiResult(json) {
        mypageApi.deleteFcmToken(DeleteFcmTokenRequest(token)).successOrThrow(json)
    }

    private fun Throwable.toFeedbackException(): Throwable = when {
        this is CommonError.Unauthorized -> FeedbackUnauthorizedException(this)
        this is CommonError.BadRequest -> FeedbackTooShortException(this)
        this is CommonError -> this
        else -> FeedbackSubmitFailedException(this)
    }

    private fun Throwable.toChangePasswordException(): Throwable = when {
        this is CommonError.Unauthorized -> InvalidCurrentPasswordException(this)
        this is CommonError.Conflict -> SocialAccountPasswordChangeNotAllowedException(this)
        this is CommonError -> this
        else -> ChangePasswordFailedException(this)
    }

    private companion object {
        val TextMediaType = "text/plain".toMediaTypeOrNull()
        val ImageMediaType = "image/*".toMediaTypeOrNull()
        const val FcmDevicePlatform = "ANDROID"
    }
}
