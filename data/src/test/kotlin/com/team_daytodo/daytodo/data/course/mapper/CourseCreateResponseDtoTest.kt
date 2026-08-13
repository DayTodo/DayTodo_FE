package com.team_daytodo.daytodo.data.course.mapper

import com.team_daytodo.daytodo.data.course.remote.dto.CourseCreateResponseDto
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class CourseCreateResponseDtoTest {
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Test
    fun `backend create response without legacy fields maps to invite link`() {
        val response = json.decodeFromString<CourseCreateResponseDto>(
            """
            {
              "courseId": 42,
              "inviteCode": "ABCD12",
              "inviteCodeExpiredAt": "2026-08-14T00:00:00"
            }
            """.trimIndent(),
        )

        val result = response.toDomain()

        assertEquals(
            "https://daytodo.app/courses/join/42?inviteCode=ABCD12",
            result.inviteLink,
        )
    }
}
