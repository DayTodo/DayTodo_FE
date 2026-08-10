package com.team_daytodo.daytodo.data.dto.magazine

import com.team_daytodo.daytodo.domain.magazine.model.Bookmark
import kotlinx.serialization.Serializable

@Serializable
data class GetBookmarksResponse(
    val bookmarks: List<BookmarkItemDto>,
)

@Serializable
data class BookmarkItemDto(
    val bookmarkId: Long,
    val magazineId: Long,
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
    thumbnailUrl = thumbnailUrl,
    placeName = placeName,
    regionName = regionName,
    category = category,
)
