package com.team_daytodo.daytodo.core.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * BE가 아직 X-User-Id 헤더 기반 임시 인증을 쓰는 API(today, mypage, place 등)에
 * 자동으로 헤더를 붙여주는 인터셉터. diary 등 이미 JWT(Authorization Bearer)로
 * 전환된 API에는 필요하지 않다 - 재사용 시 BE 컨트롤러가 어떤 방식인지 먼저 확인할 것.
 * TODO: BE JWT 전환 후 Authorization Bearer 방식으로 교체 필요, X-User-Id 임시 사용 중
 */
class UserIdAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-User-Id", TemporaryAuthConfig.TEMP_USER_ID)
            .addHeader("Authorization", "Bearer ${TemporaryAuthConfig.TEMP_ACCESS_TOKEN}")
            .build()
        return chain.proceed(request)
    }
}
