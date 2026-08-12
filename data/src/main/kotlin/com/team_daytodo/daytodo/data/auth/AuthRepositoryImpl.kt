package com.team_daytodo.daytodo.data.auth

import com.team_daytodo.daytodo.core.model.CommonError
import com.team_daytodo.daytodo.data.auth.local.AuthTokenLocalDataSource
import com.team_daytodo.daytodo.data.auth.local.SignupEmailLocalDataSource
import com.team_daytodo.daytodo.data.auth.mapper.toDto
import com.team_daytodo.daytodo.data.auth.mapper.toEmailCheckResult
import com.team_daytodo.daytodo.data.auth.mapper.toLinkNaverResult
import com.team_daytodo.daytodo.data.auth.mapper.toLoginResult
import com.team_daytodo.daytodo.data.auth.mapper.toLogoutDto
import com.team_daytodo.daytodo.data.auth.mapper.toNaverLoginResult
import com.team_daytodo.daytodo.data.auth.mapper.toPasswordResetDto
import com.team_daytodo.daytodo.data.auth.mapper.toProfileSetupResult
import com.team_daytodo.daytodo.data.auth.mapper.toQueryEmail
import com.team_daytodo.daytodo.data.auth.mapper.toQueryToken
import com.team_daytodo.daytodo.data.auth.mapper.toRegisterDto
import com.team_daytodo.daytodo.data.auth.mapper.toResetPasswordResult
import com.team_daytodo.daytodo.data.auth.mapper.toSendPasswordVerificationCodeResult
import com.team_daytodo.daytodo.data.auth.mapper.toSignupResult
import com.team_daytodo.daytodo.data.auth.mapper.toTokenRefreshDto
import com.team_daytodo.daytodo.data.auth.mapper.toTokenValues
import com.team_daytodo.daytodo.data.auth.mapper.toVerifyEmailResult
import com.team_daytodo.daytodo.data.auth.mapper.toVerifyPasswordCodeResult
import com.team_daytodo.daytodo.data.auth.remote.AuthRemoteDataSource
import com.team_daytodo.daytodo.domain.auth.model.AccountLockedException
import com.team_daytodo.daytodo.domain.auth.model.AuthErrorCode
import com.team_daytodo.daytodo.domain.auth.model.AuthException
import com.team_daytodo.daytodo.domain.auth.model.AuthLoginException
import com.team_daytodo.daytodo.domain.auth.model.AuthServerException
import com.team_daytodo.daytodo.domain.auth.model.AuthSignupException
import com.team_daytodo.daytodo.domain.auth.model.AutoLoginResult
import com.team_daytodo.daytodo.domain.auth.model.EmailAlreadyExistsException
import com.team_daytodo.daytodo.domain.auth.model.EmailCheckException
import com.team_daytodo.daytodo.domain.auth.model.EmailCheckRequest
import com.team_daytodo.daytodo.domain.auth.model.EmailCheckResult
import com.team_daytodo.daytodo.domain.auth.model.EmailVerificationException
import com.team_daytodo.daytodo.domain.auth.model.LinkNaverRequest
import com.team_daytodo.daytodo.domain.auth.model.LinkNaverResult
import com.team_daytodo.daytodo.domain.auth.model.LoginRequest
import com.team_daytodo.daytodo.domain.auth.model.LoginResult
import com.team_daytodo.daytodo.domain.auth.model.LogoutException
import com.team_daytodo.daytodo.domain.auth.model.NaverLinkException
import com.team_daytodo.daytodo.domain.auth.model.NaverLoginRequest
import com.team_daytodo.daytodo.domain.auth.model.NaverLoginResult
import com.team_daytodo.daytodo.domain.auth.model.PasswordVerificationException
import com.team_daytodo.daytodo.domain.auth.model.ProfileSetupException
import com.team_daytodo.daytodo.domain.auth.model.ProfileSetupRequest
import com.team_daytodo.daytodo.domain.auth.model.ProfileSetupResult
import com.team_daytodo.daytodo.domain.auth.model.ResendEmailVerificationRequest
import com.team_daytodo.daytodo.domain.auth.model.ResetPasswordException
import com.team_daytodo.daytodo.domain.auth.model.ResetPasswordRequest
import com.team_daytodo.daytodo.domain.auth.model.ResetPasswordResult
import com.team_daytodo.daytodo.domain.auth.model.SendPasswordVerificationCodeRequest
import com.team_daytodo.daytodo.domain.auth.model.SendPasswordVerificationCodeResult
import com.team_daytodo.daytodo.domain.auth.model.SignupRequest
import com.team_daytodo.daytodo.domain.auth.model.SignupResult
import com.team_daytodo.daytodo.domain.auth.model.VerifyEmailRequest
import com.team_daytodo.daytodo.domain.auth.model.VerifyEmailResult
import com.team_daytodo.daytodo.domain.auth.model.VerifyPasswordCodeRequest
import com.team_daytodo.daytodo.domain.auth.model.VerifyPasswordCodeResult
import com.team_daytodo.daytodo.domain.auth.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val tokenLocalDataSource: AuthTokenLocalDataSource,
    private val signupEmailLocalDataSource: SignupEmailLocalDataSource,
) : AuthRepository {
    override suspend fun login(request: LoginRequest): Result<LoginResult> =
        authResult(AuthOperation.Login) {
            val response = remoteDataSource.login(request.toDto())
            tokenLocalDataSource.save(
                tokens = response.toTokenValues(),
                keepLoggedIn = request.keepLoggedIn,
            )

            response.toLoginResult()
        }

    override suspend fun loginWithNaver(request: NaverLoginRequest): Result<NaverLoginResult> =
        authResult(AuthOperation.LoginNaver) {
            val response = remoteDataSource.loginNaver(request.toDto())
            tokenLocalDataSource.save(
                tokens = response.toTokenValues(),
                keepLoggedIn = request.keepLoggedIn,
            )

            response.toNaverLoginResult()
        }

    override suspend fun checkAutoLogin(): Result<AutoLoginResult> =
        runCatching {
            val persistedTokens = tokenLocalDataSource.getPersistedTokens()
                ?: return@runCatching AutoLoginResult(isLoggedIn = false)
            val refreshToken = persistedTokens.refreshToken
                ?: return@runCatching AutoLoginResult(isLoggedIn = false)

            runCatching {
                val response = remoteDataSource.tokenRefresh(refreshToken.toTokenRefreshDto())
                tokenLocalDataSource.save(
                    tokens = response.toTokenValues(),
                    keepLoggedIn = true,
                )
                AutoLoginResult(isLoggedIn = true)
            }.getOrElse { cause ->
                if (cause.canUseCachedSession()) {
                    AutoLoginResult(
                        isLoggedIn = true,
                        usedCachedSession = true,
                    )
                } else {
                    tokenLocalDataSource.clear()
                    AutoLoginResult(isLoggedIn = false)
                }
            }
        }.recoverCatching { cause ->
            throw cause.toAuthFailure(AuthOperation.AutoLogin)
        }

    override suspend fun logout(): Result<Unit> =
        authResult(AuthOperation.Logout) {
            try {
                tokenLocalDataSource.getRefreshToken()?.let { refreshToken ->
                    remoteDataSource.logout(refreshToken.toLogoutDto())
                }
            } finally {
                tokenLocalDataSource.clear()
            }
        }

    override suspend fun signup(request: SignupRequest): Result<SignupResult> =
        authResult(AuthOperation.Signup) {
            val result = remoteDataSource.register(request.toRegisterDto()).toSignupResult()
            // BE는 재조회 가능한 이메일 응답이 없어(회원가입 응답에만 1회성으로 내려옴),
            // 마이페이지 표시용으로 로컬에 저장해둔다. 재로그인/재설치 시엔 값이 없을 수 있다.
            signupEmailLocalDataSource.saveEmail(result.email)
            result
        }

    override suspend fun linkNaver(request: LinkNaverRequest): Result<LinkNaverResult> =
        authResult(AuthOperation.LinkNaver) {
            remoteDataSource.linkNaver(request.toDto()).toLinkNaverResult()
        }

    override suspend fun checkEmail(request: EmailCheckRequest): Result<EmailCheckResult> =
        authResult(AuthOperation.EmailCheck) {
            remoteDataSource.emailCheck(request.toQueryEmail()).toEmailCheckResult()
        }

    override suspend fun verifyEmail(request: VerifyEmailRequest): Result<VerifyEmailResult> =
        authResult(AuthOperation.VerifyEmail) {
            remoteDataSource.verifyEmail(request.toQueryToken()).toVerifyEmailResult()
        }

    override suspend fun resendEmailVerification(
        request: ResendEmailVerificationRequest,
    ): Result<Unit> =
        authResult(AuthOperation.ResendEmailVerification) {
            remoteDataSource.verifyEmailResend(request.toDto())
        }

    override suspend fun sendPasswordVerificationCode(
        request: SendPasswordVerificationCodeRequest,
    ): Result<SendPasswordVerificationCodeResult> =
        authResult(AuthOperation.SendPasswordVerificationCode) {
            remoteDataSource
                .passwordResetRequest(request.toDto())
                .toSendPasswordVerificationCodeResult()
        }

    override suspend fun verifyPasswordCode(
        request: VerifyPasswordCodeRequest,
    ): Result<VerifyPasswordCodeResult> =
        authResult(AuthOperation.VerifyPasswordCode) {
            request.toVerifyPasswordCodeResult()
        }

    override suspend fun resetPassword(request: ResetPasswordRequest): Result<ResetPasswordResult> =
        authResult(AuthOperation.ResetPassword) {
            remoteDataSource
                .passwordReset(request.toPasswordResetDto())
                .toResetPasswordResult()
        }

    override suspend fun saveProfile(request: ProfileSetupRequest): Result<ProfileSetupResult> =
        authResult(AuthOperation.SaveProfile) {
            request.toProfileSetupResult()
        }

    private suspend fun <T> authResult(
        operation: AuthOperation,
        block: suspend () -> T,
    ): Result<T> =
        runCatching {
            block()
        }.recoverCatching { cause ->
            throw cause.toAuthFailure(operation)
        }

    private fun Throwable.canUseCachedSession(): Boolean =
        this is CommonError.NetworkUnavailable ||
            this is CommonError.Timeout ||
            this is CommonError.Server

    private fun Throwable.toAuthFailure(operation: AuthOperation): Throwable =
        when {
            this is AuthException -> this
            this is CommonError.NetworkUnavailable -> this
            this is CommonError.Timeout -> this
            this is CommonError.TooManyRequests -> this
            this is CommonError.InvalidResponse -> this
            this is CommonError.EmptyBody -> this
            this is CommonError.Server -> toAuthServerExceptionOrNull() ?: this
            this is CommonError -> toAuthServerExceptionOrNull()
                ?: toOperationAuthException(operation)
            else -> operation.toAuthException(cause = this)
        }

    private fun CommonError.toAuthServerExceptionOrNull(): AuthException? {
        val authErrorCode = serverCode
            ?.let { it.toAuthErrorCodeOrNull() }
            ?: return null

        return when (authErrorCode) {
            AuthErrorCode.ACCOUNT_LOCKED -> AccountLockedException(
                cause = this,
                message = userMessage,
            )
            AuthErrorCode.EMAIL_DUPLICATED -> EmailAlreadyExistsException(
                cause = this,
                message = userMessage,
            )
            else -> AuthServerException(
                authErrorCode = authErrorCode,
                message = userMessage,
                cause = this,
            )
        }
    }

    private fun CommonError.toOperationAuthException(operation: AuthOperation): AuthException =
        when {
            this is CommonError.Unauthorized && operation.isLoginOperation() -> {
                AuthLoginException(
                    cause = this,
                    message = "이메일 또는 비밀번호를 확인해 주세요.",
                )
            }
            this is CommonError.Conflict && operation == AuthOperation.Signup -> {
                EmailAlreadyExistsException(
                    cause = this,
                    message = userMessage,
                )
            }
            else -> operation.toAuthException(
                cause = this,
                message = userMessage,
            )
        }

    private fun String.toAuthErrorCodeOrNull(): AuthErrorCode? =
        enumValues<AuthErrorCode>().firstOrNull { it.name == this }

    private fun AuthOperation.isLoginOperation(): Boolean =
        this == AuthOperation.Login || this == AuthOperation.LoginNaver

    private fun AuthOperation.toAuthException(
        cause: Throwable,
        message: String? = null,
    ): AuthException =
        when (this) {
            AuthOperation.Login,
            AuthOperation.LoginNaver,
            -> AuthLoginException(
                cause = cause,
                message = message ?: "로그인에 실패했어요. 잠시 후 다시 시도해 주세요.",
            )
            AuthOperation.AutoLogin -> AuthLoginException(
                cause = cause,
                message = message ?: "자동 로그인에 실패했어요. 다시 로그인해 주세요.",
            )
            AuthOperation.Logout -> LogoutException(
                cause = cause,
                message = message ?: "로그아웃을 완료하지 못했어요. 잠시 후 다시 시도해 주세요.",
            )
            AuthOperation.Signup -> AuthSignupException(
                cause = cause,
                message = message ?: "회원가입에 실패했어요. 잠시 후 다시 시도해 주세요.",
            )
            AuthOperation.LinkNaver -> NaverLinkException(
                cause = cause,
                message = message ?: "네이버 계정을 연동하지 못했어요. 잠시 후 다시 시도해 주세요.",
            )
            AuthOperation.EmailCheck -> EmailCheckException(
                cause = cause,
                message = message ?: "이메일을 확인하지 못했어요. 잠시 후 다시 시도해 주세요.",
            )
            AuthOperation.VerifyEmail,
            AuthOperation.ResendEmailVerification,
            -> EmailVerificationException(
                cause = cause,
                message = message ?: "이메일 인증을 처리하지 못했어요. 잠시 후 다시 시도해 주세요.",
            )
            AuthOperation.SendPasswordVerificationCode,
            AuthOperation.VerifyPasswordCode,
            -> PasswordVerificationException(
                message = message ?: "인증코드를 확인하지 못했어요. 다시 시도해 주세요.",
                cause = cause,
            )
            AuthOperation.ResetPassword -> ResetPasswordException(
                cause = cause,
                message = message ?: "비밀번호를 변경하지 못했어요. 잠시 후 다시 시도해 주세요.",
            )
            AuthOperation.SaveProfile -> ProfileSetupException(
                cause = cause,
                message = message ?: "프로필을 저장하지 못했어요. 잠시 후 다시 시도해 주세요.",
            )
        }

    private enum class AuthOperation {
        Login,
        LoginNaver,
        AutoLogin,
        Logout,
        Signup,
        LinkNaver,
        EmailCheck,
        VerifyEmail,
        ResendEmailVerification,
        SendPasswordVerificationCode,
        VerifyPasswordCode,
        ResetPassword,
        SaveProfile,
    }
}
