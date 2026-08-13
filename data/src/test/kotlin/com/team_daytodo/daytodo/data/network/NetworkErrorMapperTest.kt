package com.team_daytodo.daytodo.data.network

import com.team_daytodo.daytodo.core.model.CommonError
import java.net.UnknownHostException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class NetworkErrorMapperTest {
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Test
    fun `safeApiResult maps network exceptions to user facing common errors`() = runTest {
        val result = safeApiResult(json) {
            throw UnknownHostException()
        }

        val error = result.exceptionOrNull()

        assertTrue(error is CommonError.NetworkUnavailable)
        assertEquals("인터넷 연결을 확인해 주세요.", error?.message)
    }

    @Test(expected = CancellationException::class)
    fun `safeApiResult rethrows coroutine cancellation`() = runTest {
        safeApiResult(json) {
            throw CancellationException()
        }
    }

    @Test
    fun `toHttpError keeps server error code and message`() {
        val response = Response.error<Unit>(
            429,
            """
            {
              "code": "TOO_MANY_REQUESTS",
              "message": "잠시 후 다시 시도해 주세요."
            }
            """.trimIndent().toResponseBody("application/json".toMediaType()),
        )

        val error = response.toHttpError(json)

        assertTrue(error is CommonError.TooManyRequests)
        assertEquals(429, error.statusCode)
        assertEquals("TOO_MANY_REQUESTS", error.serverCode)
        assertEquals("잠시 후 다시 시도해 주세요.", error.message)
    }
}
