package com.team_daytodo.daytodo.data.di

import com.team_daytodo.daytodo.data.BuildConfig
import com.team_daytodo.daytodo.data.api.CalendarApi
import com.team_daytodo.daytodo.data.api.MypageApi
import com.team_daytodo.daytodo.data.api.TodayApi
import com.team_daytodo.daytodo.data.auth.local.AuthTokenLocalDataSource
import com.team_daytodo.daytodo.data.network.RetrofitFactory
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DayTodoBaseUrl

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    @DayTodoBaseUrl
    fun provideDayTodoBaseUrl(): String = BuildConfig.DAYTODO_BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authTokenLocalDataSource: AuthTokenLocalDataSource,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Timber.tag("OkHttp").d(message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val accessToken = authTokenLocalDataSource.getAccessToken()
                val originalRequest = chain.request()
                val requestBuilder = originalRequest
                    .newBuilder()
                    .header(AcceptHeaderName, ApplicationJson)

                if (
                    !accessToken.isNullOrBlank() &&
                    originalRequest.url.encodedPath.requiresAuthorization()
                ) {
                    requestBuilder
                        .header(AuthorizationHeaderName, "Bearer $accessToken")
                }

                chain.proceed(requestBuilder.build())
            }
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideTodayApi(
        retrofitFactory: RetrofitFactory,
        @DayTodoBaseUrl baseUrl: String,
    ): TodayApi = retrofitFactory.create(baseUrl).create(TodayApi::class.java)

    @Provides
    @Singleton
    fun provideMypageApi(
        retrofitFactory: RetrofitFactory,
        @DayTodoBaseUrl baseUrl: String,
    ): MypageApi = retrofitFactory.create(baseUrl).create(MypageApi::class.java)

    @Provides
    @Singleton
    fun provideCalendarApi(
        retrofitFactory: RetrofitFactory,
        @DayTodoBaseUrl baseUrl: String,
    ): CalendarApi = retrofitFactory.create(baseUrl).create(CalendarApi::class.java)

    private fun String.requiresAuthorization(): Boolean =
        PublicAuthPaths.none { endsWith(it) }

    private val PublicAuthPaths = setOf(
        "/auth/login",
        "/auth/login/naver",
        "/auth/logout",
        "/auth/register",
        "/auth/email-check",
        "/auth/verify-email",
        "/auth/verify-email/resend",
        "/auth/password/reset-request",
        "/auth/password/reset",
        "/auth/token/refresh",
    )

    private const val AuthorizationHeaderName = "Authorization"
    private const val AcceptHeaderName = "Accept"
    private const val ApplicationJson = "application/json"
}
