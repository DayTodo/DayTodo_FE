package com.team_daytodo.daytodo.data.dto.record

import com.team_daytodo.daytodo.domain.record.model.RecordPlaceBookmark
import kotlinx.serialization.Serializable

@Serializable
data class CreatePlaceBookmarkResponse(
    val bookmarkId: Long,
    val placeId: Long,
)

fun CreatePlaceBookmarkResponse.toDomain(): RecordPlaceBookmark = RecordPlaceBookmark(
    bookmarkId = bookmarkId,
    placeId = placeId,
)
