package com.team_daytodo.daytodo.data.dto.today

import com.team_daytodo.daytodo.domain.today.model.TodayCoursePlace
import kotlinx.serialization.Serializable

@Serializable
data class ReorderCoursePlacesRequest(
    val orderedCoursePlaceIds: List<Long>,
)

// 장소 추가/순서변경 응답: BE가 투데이 조회와 동일한 형태(TodayCoursePlaceDto)로 전체 목록을 반환한다.
@Serializable
data class GetCoursePlacesResponse(
    val places: List<TodayCoursePlaceDto>,
)

fun GetCoursePlacesResponse.toDomain(): List<TodayCoursePlace> = places.map { it.toDomain() }
