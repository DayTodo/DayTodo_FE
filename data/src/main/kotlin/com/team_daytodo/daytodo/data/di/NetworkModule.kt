package com.team_daytodo.daytodo.data.di

import com.team_daytodo.daytodo.core.network.NetworkConfig
import com.team_daytodo.daytodo.core.network.UserIdAuthInterceptor
import com.team_daytodo.daytodo.data.api.TodayApi
import com.team_daytodo.daytodo.data.network.RetrofitFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

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
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Timber.tag("OkHttp").d(message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(UserIdAuthInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideTodayApi(retrofitFactory: RetrofitFactory): TodayApi =
        retrofitFactory.create(NetworkConfig.BASE_URL).create(TodayApi::class.java)
}
