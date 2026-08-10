package com.team_daytodo.daytodo.domain.course.usecase

import com.team_daytodo.daytodo.domain.course.model.CourseRegionLoadException
import com.team_daytodo.daytodo.domain.course.model.CourseRegionGroup
import com.team_daytodo.daytodo.domain.course.repository.CourseRepository
import javax.inject.Inject

class GetCourseRegionsUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(): Result<List<CourseRegionGroup>> =
        courseRepository.getCourseRegions().fold(
            onSuccess = { regions ->
                val availableRegions = regions.mapNotNull { region ->
                    val name = region.name.trim()
                    val children = region.children
                        .map(String::trim)
                        .filter(String::isNotBlank)

                    if (name.isBlank()) {
                        null
                    } else {
                        region.copy(name = name, children = children)
                    }
                }
                if (availableRegions.isEmpty()) {
                    Result.failure(CourseRegionLoadException())
                } else {
                    Result.success(availableRegions)
                }
            },
            onFailure = { cause ->
                Result.failure(CourseRegionLoadException(cause))
            },
        )
}
