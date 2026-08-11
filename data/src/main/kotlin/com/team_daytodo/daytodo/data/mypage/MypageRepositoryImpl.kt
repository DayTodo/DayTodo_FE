package com.team_daytodo.daytodo.data.mypage

import android.content.Context
import android.net.Uri
import com.team_daytodo.daytodo.data.api.MypageApi
import com.team_daytodo.daytodo.data.dto.mypage.ChangePasswordRequest
import com.team_daytodo.daytodo.data.dto.mypage.DeleteFcmTokenRequest
import com.team_daytodo.daytodo.data.dto.mypage.LogoutRequest
import com.team_daytodo.daytodo.data.dto.mypage.RegisterFcmTokenRequest
import com.team_daytodo.daytodo.data.dto.mypage.SendFeedbackRequest
import com.team_daytodo.daytodo.data.dto.mypage.UpdateInterestRegionsRequest
import com.team_daytodo.daytodo.data.dto.mypage.UpdateNotificationSettingsRequest
import com.team_daytodo.daytodo.data.dto.mypage.toDomain
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import retrofit2.Response

class MypageRepositoryImpl @Inject constructor(
    private val mypageApi: MypageApi,
    @ApplicationContext private val context: Context,
) : MypageRepository {
    override suspend fun getProfile(): Result<MypageProfile> = runCatching {
        mypageApi.getProfile().toDomain()
    }

    override suspend fun updateProfile(nickname: String, profileImageUri: String?): Result<MypageProfile> =
        runCatching {
            val nicknamePart = nickname.toRequestBody(TextMediaType)
            val imagePart = profileImageUri?.let { uri -> createImagePart(uri) }
            mypageApi.updateProfile(nicknamePart, imagePart).toDomain()
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

    override suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> = runCatching {
        mypageApi.changePassword(ChangePasswordRequest(currentPassword, newPassword)).throwIfNotSuccessful()
    }.recoverCatching { cause -> throw cause.toChangePasswordException() }

    override suspend fun getNotificationSettings(): Result<Boolean> = runCatching {
        mypageApi.getNotificationSettings().pushEnabled
    }

    override suspend fun setNotificationEnabled(enabled: Boolean): Result<Unit> = runCatching {
        mypageApi.updateNotificationSettings(UpdateNotificationSettingsRequest(enabled))
        Unit
    }

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
    }.recoverCatching { cause -> throw cause.toFeedbackException() }

    override suspend fun logout(refreshToken: String): Result<Unit> = runCatching {
        mypageApi.logout(LogoutRequest(refreshToken)).throwIfNotSuccessful()
    }

    override suspend fun registerFcmToken(token: String): Result<Unit> = runCatching {
        mypageApi.registerFcmToken(RegisterFcmTokenRequest(token, FcmDevicePlatform)).throwIfNotSuccessful()
    }

    override suspend fun deleteFcmToken(token: String): Result<Unit> = runCatching {
        mypageApi.deleteFcmToken(DeleteFcmTokenRequest(token)).throwIfNotSuccessful()
    }

    private fun Response<Unit>.throwIfNotSuccessful() {
        if (!isSuccessful) throw HttpException(this)
    }

    private fun Throwable.toFeedbackException(): Throwable = when {
        this is HttpException && code() == 401 -> FeedbackUnauthorizedException(this)
        this is HttpException && code() == 400 -> FeedbackTooShortException(this)
        else -> FeedbackSubmitFailedException(this)
    }

    private fun Throwable.toChangePasswordException(): Throwable = when {
        this is HttpException && code() == 401 -> InvalidCurrentPasswordException(this)
        this is HttpException && code() == 409 -> SocialAccountPasswordChangeNotAllowedException(this)
        else -> ChangePasswordFailedException(this)
    }

    private companion object {
        val TextMediaType = "text/plain".toMediaTypeOrNull()
        val ImageMediaType = "image/*".toMediaTypeOrNull()
        const val FcmDevicePlatform = "ANDROID"
    }
}
