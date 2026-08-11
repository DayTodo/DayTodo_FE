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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    /** 자체 로그인 */
    @POST("$AuthApiPrefix/login")
    suspend fun login(
        @Body request: LoginRequestDto,
    ): Response<LoginResponseDto>

    /** 네이버 로그인 */
    @POST("$AuthApiPrefix/login/naver")
    suspend fun loginNaver(
        @Body request: LoginNaverRequestDto,
    ): Response<LoginNaverResponseDto>

    /** 로그아웃 */
    @POST("$AuthApiPrefix/logout")
    suspend fun logout(
        @Body request: LogoutRequestDto,
    ): Response<Unit>

    /** 토큰 재발급 */
    @POST("$AuthApiPrefix/token/refresh")
    suspend fun tokenRefresh(
        @Body request: TokenRefreshRequestDto,
    ): Response<TokenRefreshResponseDto>

    /** 계정 연동 처리 */
    @POST("$AuthApiPrefix/link/naver")
    suspend fun linkNaver(
        @Body request: LinkNaverRequestDto,
    ): Response<LinkNaverResponseDto>

    /** 이메일 중복 확인 */
    @GET("$AuthApiPrefix/email-check")
    suspend fun emailCheck(
        @Query("email") email: String,
    ): Response<EmailCheckResponseDto>

    /** 회원 가입 */
    @POST("$AuthApiPrefix/register")
    suspend fun register(
        @Body request: RegisterRequestDto,
    ): Response<RegisterResponseDto>

    /** 이메일 인증 처리 */
    @GET("$AuthApiPrefix/verify-email")
    suspend fun verifyEmail(
        @Query("token") token: String,
    ): Response<VerifyEmailResponseDto>

    /** 인증 메일 재전송 */
    @POST("$AuthApiPrefix/verify-email/resend")
    suspend fun verifyEmailResend(
        @Body request: VerifyEmailResendRequestDto,
    ): Response<Unit>

    /** 비밀번호 재설정 요청 */
    @POST("$AuthApiPrefix/password/reset-request")
    suspend fun passwordResetRequest(
        @Body request: PasswordResetRequestRequestDto,
    ): Response<Unit>

    /** 비밀번호 재설정 */
    @POST("$AuthApiPrefix/password/reset")
    suspend fun passwordReset(
        @Body request: PasswordResetRequestDto,
    ): Response<Unit>

    private companion object {
        const val AuthApiPrefix = "auth"
    }
}
