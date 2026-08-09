package com.team_daytodo.daytodo.data.auth.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthTokenLocalDataSource @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val preferences = context.getSharedPreferences(AuthPreferencesName, Context.MODE_PRIVATE)
    private var cachedTokens: AuthTokenValues? = readPersistedTokens()

    fun getAccessToken(): String? =
        cachedTokens?.accessToken ?: readPersistedTokens()?.also { cachedTokens = it }?.accessToken

    fun getRefreshToken(): String? =
        cachedTokens?.refreshToken ?: readPersistedTokens()?.also { cachedTokens = it }?.refreshToken

    fun getPersistedTokens(): AuthTokenValues? =
        readPersistedTokens()?.also { cachedTokens = it }

    fun hasSavedSession(): Boolean =
        !getRefreshToken().isNullOrBlank()

    fun save(
        tokens: AuthTokenValues,
        keepLoggedIn: Boolean,
    ) {
        cachedTokens = tokens

        if (keepLoggedIn) {
            preferences.edit()
                .putString(AccessTokenKey, tokens.accessToken)
                .putString(RefreshTokenKey, tokens.refreshToken)
                .apply()
        } else {
            preferences.edit()
                .remove(AccessTokenKey)
                .remove(RefreshTokenKey)
                .apply()
        }
    }

    fun clear() {
        cachedTokens = null
        preferences.edit().clear().apply()
    }

    private fun readPersistedTokens(): AuthTokenValues? {
        val accessToken = preferences.getString(AccessTokenKey, null)?.takeIf(String::isNotBlank)
            ?: return null
        val refreshToken = preferences.getString(RefreshTokenKey, null)?.takeIf(String::isNotBlank)
            ?: return null

        return AuthTokenValues(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    private companion object {
        const val AuthPreferencesName = "daytodo_auth"
        const val AccessTokenKey = "access_token"
        const val RefreshTokenKey = "refresh_token"
    }
}
