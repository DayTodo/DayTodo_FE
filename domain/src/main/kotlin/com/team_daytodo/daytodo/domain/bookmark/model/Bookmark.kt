package com.team_daytodo.daytodo.domain.bookmark.model

data class Bookmark(
    val bookmarkId: Long,
    val magazineId: Long,
    // BE가 아직 응답에 내려주지 않아(BookmarkPlaceConverter 참고) 현재는 항상 null.
    // 코스에 담긴 실제 Place PK로, 이 값이 있어야만 "코스로 불러오기"가 가능하다.
    val placeId: Long?,
    val thumbnailUrl: String?,
    val placeName: String,
    val regionName: String,
    val category: String,
)
