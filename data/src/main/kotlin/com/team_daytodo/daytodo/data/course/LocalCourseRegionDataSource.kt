package com.team_daytodo.daytodo.data.course

import com.team_daytodo.daytodo.domain.course.model.CourseRegionGroup
import javax.inject.Inject

class LocalCourseRegionDataSource @Inject constructor() {
    fun getRegions(): List<CourseRegionGroup> = courseRegions

    fun getRegionId(regionName: String): Long? =
        regionIdByName[regionName.trim()]

    fun getRegionName(regionId: Long): String? =
        regionNameById[regionId]

    private companion object {
        val courseRegionCatalog = listOf(
            CourseRegionCatalogEntry(
                regionId = 1,
                regionName = "서울특별시",
                regionLevel = "SIDO",
                parentRegionId = null,
                parentRegionName = null,
            ),
            CourseRegionCatalogEntry(2, "종로구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(3, "중구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(4, "용산구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(5, "성동구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(6, "광진구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(7, "동대문구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(8, "중랑구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(9, "성북구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(10, "강북구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(11, "도봉구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(12, "노원구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(13, "은평구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(14, "서대문구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(15, "마포구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(16, "양천구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(17, "강서구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(18, "구로구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(19, "금천구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(20, "영등포구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(21, "동작구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(22, "관악구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(23, "서초구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(24, "강남구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(25, "송파구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(26, "강동구", "SIGUNGU", 1, "서울특별시"),
        )

        val courseRegions = courseRegionCatalog
            .filter { it.regionLevel == "SIDO" }
            .map { parent ->
                CourseRegionGroup(
                    name = parent.regionName,
                    children = listOf(parent.regionName) +
                        courseRegionCatalog
                            .filter { it.parentRegionId == parent.regionId }
                            .map { it.regionName },
                )
            }

        val regionIdByName = courseRegionCatalog.associate { region ->
            region.regionName to region.regionId
        }

        val regionNameById = courseRegionCatalog.associate { region ->
            region.regionId to region.regionName
        }
    }
}

private data class CourseRegionCatalogEntry(
    val regionId: Long,
    val regionName: String,
    val regionLevel: String,
    val parentRegionId: Long?,
    val parentRegionName: String?,
)
