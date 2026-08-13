package com.team_daytodo.daytodo.data.dto.bookmark

import com.team_daytodo.daytodo.domain.bookmark.model.Bookmark
import kotlinx.serialization.Serializable

@Serializable
data class GetBookmarksResponse(
    val bookmarks: List<BookmarkItemDto>,
)

@Serializable
data class BookmarkItemDto(
    val bookmarkId: Long,
    val magazineId: Long,
    // BE(BookmarkPlaceConverter)가 현재 응답에 실어 보내지 않는 값. 북마크마다 실제로는
    // 연결된 Place가 있지만(place.getPlaceId()), 아직 이 DTO로 노출되지 않아 항상 null로
    // 들어온다. BE가 필드를 내려주기 시작하면 별도 FE 수정 없이 그대로 채워진다.
    val placeId: Long? = null,
    val thumbnailUrl: String? = null,
    val placeName: String,
    val regionName: String,
    val category: String,
)

@Serializable
data class CreateBookmarkRequest(
    val contentId: Long,
)

@Serializable
data class CreateBookmarkResponse(
    val bookmarkId: Long,
    val placeId: Long,
)

fun BookmarkItemDto.toDomain(): Bookmark = Bookmark(
    bookmarkId = bookmarkId,
    magazineId = magazineId,
    placeId = placeId,
    thumbnailUrl = thumbnailUrl,
    placeName = placeName,
    regionName = regionName,
    category = category,
)
