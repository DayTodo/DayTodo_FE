package com.team_daytodo.daytodo.data.auth.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
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
    @param:ApplicationContext private val context: Context,
) {
    private var cachedTokens: AuthTokenValues? = null

    fun getAccessToken(): String? =
        cachedTokens?.accessToken ?: runBlocking {
            readPersistedTokens()?.also { cachedTokens = it }?.accessToken
        }

    fun getRefreshToken(): String? =
        cachedTokens?.refreshToken ?: runBlocking {
            readRefreshToken()?.also { refreshToken ->
                readAccessToken()?.let { accessToken ->
                    cachedTokens = AuthTokenValues(
                        accessToken = accessToken,
                        refreshToken = refreshToken,
                    )
                }
            }
        }

    suspend fun getPersistedTokens(): AuthTokenValues? =
        readPersistedTokens()?.also { cachedTokens = it }

    fun hasSavedSession(): Boolean =
        !getRefreshToken().isNullOrBlank()

    suspend fun save(
        tokens: AuthTokenValues,
        keepLoggedIn: Boolean,
    ) {
        cachedTokens = tokens

        if (keepLoggedIn) {
            context.authTokenDataStore.edit { preferences ->
                preferences[AccessTokenKey] = tokens.accessToken
                tokens.refreshToken
                    ?.takeIf(String::isNotBlank)
                    ?.let { preferences[RefreshTokenKey] = it }
                    ?: preferences.remove(RefreshTokenKey)
            }
        } else {
            clearPersistedTokens()
        }
    }

    suspend fun saveAccessToken(accessToken: String) {
        cachedTokens = AuthTokenValues(
            accessToken = accessToken,
            refreshToken = cachedTokens?.refreshToken ?: readRefreshToken(),
        )

        context.authTokenDataStore.edit { preferences ->
            preferences[AccessTokenKey] = accessToken
        }
    }

    suspend fun clearAccessToken() {
        cachedTokens = null

        context.authTokenDataStore.edit { preferences ->
            preferences.remove(AccessTokenKey)
        }
    }

    suspend fun saveRefreshToken(refreshToken: String) {
        val accessToken = cachedTokens?.accessToken ?: readAccessToken()

        cachedTokens = accessToken?.let {
            AuthTokenValues(
                accessToken = it,
                refreshToken = refreshToken,
            )
        }

        context.authTokenDataStore.edit { preferences ->
            preferences[RefreshTokenKey] = refreshToken
        }
    }

    suspend fun clearRefreshToken() {
        cachedTokens = cachedTokens?.copy(refreshToken = null)

        context.authTokenDataStore.edit { preferences ->
            preferences.remove(RefreshTokenKey)
        }
    }

    suspend fun clear() {
        cachedTokens = null
        clearPersistedTokens()
    }

    private suspend fun clearPersistedTokens() {
        context.authTokenDataStore.edit { preferences ->
            preferences.remove(AccessTokenKey)
            preferences.remove(RefreshTokenKey)
        }
    }

    private suspend fun readPersistedTokens(): AuthTokenValues? {
        val preferences = readAuthPreferences()
        val accessToken = preferences[AccessTokenKey]?.takeIf(String::isNotBlank)
            ?: return null

        return AuthTokenValues(
            accessToken = accessToken,
            refreshToken = preferences[RefreshTokenKey]?.takeIf(String::isNotBlank),
        )
    }

    private suspend fun readAccessToken(): String? =
        readAuthPreferences()[AccessTokenKey]?.takeIf(String::isNotBlank)

    private suspend fun readRefreshToken(): String? =
        readAuthPreferences()[RefreshTokenKey]?.takeIf(String::isNotBlank)

    private suspend fun readAuthPreferences(): Preferences =
        context.authTokenDataStore.data
            .catch { cause ->
                if (cause is IOException) emit(emptyPreferences()) else throw cause
            }
            .first()

    private companion object {
        val AccessTokenKey = stringPreferencesKey("access_token")
        val RefreshTokenKey = stringPreferencesKey("refresh_token")
    }
}
