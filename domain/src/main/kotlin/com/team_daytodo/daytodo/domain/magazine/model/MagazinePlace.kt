package com.team_daytodo.daytodo.domain.magazine.model

data class MagazinePlace(
    val id: String,
    val name: String,
    val region: String,
    val address: String,
    val category: String,
    val categoryPath: List<String>,
    val description: String,
    val phoneNumber: String,
    val imageUrl: String?,
    val savedAtMillis: Long?,
    val popularity: Int,
    val isSaved: Boolean,
) {
    val categoryPathText: String
        get() = categoryPath.joinToString(" > ")
}

enum class SavedPlaceSortType {
    RecentSaved,
    OldestSaved,
    Name,
    Popularity,
}

class MagazinePlaceNotFoundException(
    placeId: String,
) : NoSuchElementException("매거진 장소를 찾을 수 없어요. placeId=$placeId")
