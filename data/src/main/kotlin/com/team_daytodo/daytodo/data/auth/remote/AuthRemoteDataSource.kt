package com.team_daytodo.daytodo.data.auth.remote

import com.team_daytodo.daytodo.data.auth.remote.dto.EmailCheckResponseDto
import com.team_daytodo.daytodo.data.auth.remote.dto.LinkNaverRequestDto
import com.team_daytodo.daytodo.data.auth.remote.dto.LinkNaverResponseDto
import com.team_daytodo.daytodo.data.auth.remote.dto.LoginNaverRequestDto
import com.team_daytodo.daytodo.data.auth.remote.dto.LoginNaverResponseDto
import com.team_daytodo.daytodo.data.auth.remote.dto.LoginRequestDto
import com.team_daytodo.daytodo.data.auth.remote.dto.LoginResponseDto
import com.team_daytodo.daytodo.data.auth.remote.dto.LogoutRequestDto
import com.team_daytodo.daytodo.data.auth.remote.dto.PasswordResetRequestDto
import com.team_daytodo.daytodo.data.auth.remote.dto.PasswordResetRequestRequestDto
import com.team_daytodo.daytodo.data.auth.remote.dto.RegisterRequestDto
import com.team_daytodo.daytodo.data.auth.remote.dto.RegisterResponseDto
import com.team_daytodo.daytodo.data.auth.remote.dto.TokenRefreshRequestDto
import com.team_daytodo.daytodo.data.auth.remote.dto.TokenRefreshResponseDto
import com.team_daytodo.daytodo.data.auth.remote.dto.VerifyEmailResendRequestDto
import com.team_daytodo.daytodo.data.auth.remote.dto.VerifyEmailResponseDto
import com.team_daytodo.daytodo.data.network.bodyOrThrow
import com.team_daytodo.daytodo.data.network.safeApiCall
import com.team_daytodo.daytodo.data.network.successOrThrow
import javax.inject.Inject
import kotlinx.serialization.json.Json

class AuthRemoteDataSource @Inject constructor(
    private val authApi: AuthApi,
    private val json: Json,
) {
    suspend fun login(request: LoginRequestDto): LoginResponseDto =
        safeApiCall(json) {
            authApi.login(request).bodyOrThrow(
                json = json,
                endpoint = LoginEndpoint,
            )
        }

    suspend fun loginNaver(request: LoginNaverRequestDto): LoginNaverResponseDto =
        safeApiCall(json) {
            authApi.loginNaver(request).bodyOrThrow(
                json = json,
                endpoint = LoginNaverEndpoint,
            )
        }

    suspend fun logout(request: LogoutRequestDto) {
        safeApiCall(json) {
            authApi.logout(request).successOrThrow(json)
        }
    }

    suspend fun tokenRefresh(request: TokenRefreshRequestDto): TokenRefreshResponseDto =
        safeApiCall(json) {
            authApi.tokenRefresh(request).bodyOrThrow(
                json = json,
                endpoint = TokenRefreshEndpoint,
            )
        }

    suspend fun linkNaver(request: LinkNaverRequestDto): LinkNaverResponseDto =
        safeApiCall(json) {
            authApi.linkNaver(request).bodyOrThrow(
                json = json,
                endpoint = LinkNaverEndpoint,
            )
        }

    suspend fun emailCheck(email: String): EmailCheckResponseDto =
        safeApiCall(json) {
            authApi.emailCheck(email).bodyOrThrow(
                json = json,
                endpoint = EmailCheckEndpoint,
            )
        }

    suspend fun register(request: RegisterRequestDto): RegisterResponseDto =
        safeApiCall(json) {
            authApi.register(request).bodyOrThrow(
                json = json,
                endpoint = RegisterEndpoint,
            )
        }

    suspend fun verifyEmail(token: String): VerifyEmailResponseDto =
        safeApiCall(json) {
            authApi.verifyEmail(token).bodyOrThrow(
                json = json,
                endpoint = VerifyEmailEndpoint,
            )
        }

    suspend fun verifyEmailResend(request: VerifyEmailResendRequestDto) {
        safeApiCall(json) {
            authApi.verifyEmailResend(request).successOrThrow(json)
        }
    }

    suspend fun passwordResetRequest(request: PasswordResetRequestRequestDto) {
        safeApiCall(json) {
            authApi.passwordResetRequest(request).successOrThrow(json)
        }
    }

    suspend fun passwordReset(request: PasswordResetRequestDto) {
        safeApiCall(json) {
            authApi.passwordReset(request).successOrThrow(json)
        }
    }

    private companion object {
        const val LoginEndpoint = "POST auth/login"
        const val LoginNaverEndpoint = "POST auth/login/naver"
        const val TokenRefreshEndpoint = "POST auth/token/refresh"
        const val LinkNaverEndpoint = "POST auth/link/naver"
        const val EmailCheckEndpoint = "GET auth/email-check"
        const val RegisterEndpoint = "POST auth/register"
        const val VerifyEmailEndpoint = "GET auth/verify-email"
    }
}
