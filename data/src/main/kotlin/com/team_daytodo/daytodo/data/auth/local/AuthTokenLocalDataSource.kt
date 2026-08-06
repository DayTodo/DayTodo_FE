package com.team_daytodo.daytodo.data.auth.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private val Context.authTokenDataStore by preferencesDataStore(name = "daytodo_auth_token")

@Singleton
class AuthTokenLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    // OkHttp Interceptor는 suspend 함수를 호출할 수 없어 동기 API로 노출한다.
    fun getAccessToken(): String? = runBlocking {
        context.authTokenDataStore.data
            .catch { cause ->
                if (cause is IOException) emit(emptyPreferences()) else throw cause
            }
            .first()[AccessTokenKey]
    }

    suspend fun saveAccessToken(accessToken: String) {
        context.authTokenDataStore.edit { preferences ->
            preferences[AccessTokenKey] = accessToken
        }
    }

    suspend fun clearAccessToken() {
        context.authTokenDataStore.edit { preferences ->
            preferences.remove(AccessTokenKey)
        }
    }

    private companion object {
        val AccessTokenKey = stringPreferencesKey("access_token")
    }
}
