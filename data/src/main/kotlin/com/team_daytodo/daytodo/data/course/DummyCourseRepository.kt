package com.team_daytodo.daytodo.data.course

import com.team_daytodo.daytodo.domain.course.model.CourseCreateRequest
import com.team_daytodo.daytodo.domain.course.model.CourseCreateResult
import com.team_daytodo.daytodo.domain.course.model.CourseRegionGroup
import com.team_daytodo.daytodo.domain.course.model.CourseRegionLoadException
import com.team_daytodo.daytodo.domain.course.model.CourseRoomCreateException
import com.team_daytodo.daytodo.domain.course.repository.CourseRepository
import javax.inject.Inject
import kotlinx.coroutines.delay

class DummyCourseRepository @Inject constructor(
    private val localCourseRegionDataSource: LocalCourseRegionDataSource,
) : CourseRepository {
    override suspend fun getCourseRegions(): Result<List<CourseRegionGroup>> =
        runCatching {
            localCourseRegionDataSource.getRegions()
        }.recoverCatching { cause ->
            throw CourseRegionLoadException(cause)
        }

    override suspend fun createCourseRoom(
        request: CourseCreateRequest,
    ): Result<CourseCreateResult> =
        runCatching {
            delay(CourseCreateDelayMillis)

            CourseCreateResult(
                inviteLink = buildInviteLink(request),
            )
        }.recoverCatching { cause ->
            throw CourseRoomCreateException(cause)
        }

    private fun buildInviteLink(request: CourseCreateRequest): String {
        val roomHash = request.roomName
            .trim()
            .hashCode()
            .toUInt()
            .toString(radix = InviteHashRadix)

        return "$InviteBaseUrl/$roomHash"
    }

    private companion object {
        const val CourseCreateDelayMillis = 1200L
        const val InviteBaseUrl = "https://daytodo.app/invite/course"
        const val InviteHashRadix = 16
    }
}
