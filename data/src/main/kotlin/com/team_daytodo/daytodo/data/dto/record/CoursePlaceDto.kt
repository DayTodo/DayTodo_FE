package com.team_daytodo.daytodo.data.dto.record

import com.team_daytodo.daytodo.domain.record.model.RecordPlace
import kotlinx.serialization.Serializable

@Serializable
data class CoursePlaceDto(
    val coursePlaceId: Long,
    val placeId: Long,
    val placeName: String,
    val placeOrder: Int,
)

fun CoursePlaceDto.toDomain(): RecordPlace = RecordPlace(
    coursePlaceId = coursePlaceId,
    placeId = placeId,
    placeName = placeName,
    placeOrder = placeOrder,
)
