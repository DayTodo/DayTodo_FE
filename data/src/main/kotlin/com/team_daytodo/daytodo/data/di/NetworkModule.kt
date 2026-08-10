package com.team_daytodo.daytodo.data.di

import com.team_daytodo.daytodo.data.BuildConfig
import com.team_daytodo.daytodo.data.api.CalendarApi
import com.team_daytodo.daytodo.data.api.MagazineApi
import com.team_daytodo.daytodo.data.api.MypageApi
import com.team_daytodo.daytodo.data.api.RecordApi
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
                val request = if (accessToken.isNullOrBlank()) {
                    chain.request()
                } else {
                    chain.request()
                        .newBuilder()
                        .header(AuthorizationHeaderName, "Bearer $accessToken")
                        .build()
                }

                chain.proceed(request)
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

    @Provides
    @Singleton
    fun provideRecordApi(
        retrofitFactory: RetrofitFactory,
        @DayTodoBaseUrl baseUrl: String,
    ): RecordApi = retrofitFactory.create(baseUrl).create(RecordApi::class.java)

    @Provides
    @Singleton
    fun provideMagazineApi(
        retrofitFactory: RetrofitFactory,
        @DayTodoBaseUrl baseUrl: String,
    ): MagazineApi = retrofitFactory.create(baseUrl).create(MagazineApi::class.java)

    private const val AuthorizationHeaderName = "Authorization"
}
