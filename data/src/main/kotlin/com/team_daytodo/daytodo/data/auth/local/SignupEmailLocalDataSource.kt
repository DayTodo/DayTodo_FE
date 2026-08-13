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

private val Context.signupEmailDataStore by preferencesDataStore(name = "daytodo_signup_email")

// 마이페이지에 표시할 이메일의 유일한 로컬 소스. BE의 어떤 재조회형 응답(GET /users/profile 등)에도
// email 필드가 없고, 회원가입 응답(POST /auth/register)에만 1회성으로 내려오기 때문에 그 시점에
// 저장해둔다. 재로그인이나 앱 재설치처럼 회원가입을 거치지 않고 세션을 얻은 경우에는 값이 없을 수 있다.
@Singleton
class SignupEmailLocalDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    suspend fun saveEmail(email: String) {
        context.signupEmailDataStore.edit { preferences ->
            preferences[EmailKey] = email
        }
    }

    suspend fun getEmail(): String? =
        context.signupEmailDataStore.data
            .catch { cause -> if (cause is IOException) emit(emptyPreferences()) else throw cause }
            .first()[EmailKey]
            ?.takeIf(String::isNotBlank)

    private companion object {
        val EmailKey = stringPreferencesKey("signup_email")
    }
}
