package com.team_daytodo.daytodo.feature.save.model

import com.team_daytodo.daytodo.domain.bookmark.model.Bookmark
import com.team_daytodo.daytodo.domain.bookmark.model.SavedPlaceSortType
import com.team_daytodo.daytodo.domain.region.model.Region

data class SaveUiState(
    val isLoading: Boolean = false,
    val places: List<Bookmark> = emptyList(),
    val sortType: SavedPlaceSortType = SavedPlaceSortType.RecentSaved,
    val isSortDialogVisible: Boolean = false,
    val regions: List<Region> = emptyList(),
    val selectedRegionId: Long? = null,
    val isRegionDialogVisible: Boolean = false,
    val errorMessage: String? = null,
) {
    val selectedRegionName: String?
        get() = regions.find { it.regionId == selectedRegionId }?.regionName
}

sealed interface SaveEvent {
    data class ShowMessage(val message: String) : SaveEvent
}

data class SavedPlacePickerUiState(
    val isLoading: Boolean = false,
    val isImporting: Boolean = false,
    val places: List<Bookmark> = emptyList(),
    val selectedPlaceIds: Set<String> = emptySet(),
    val sortType: SavedPlaceSortType = SavedPlaceSortType.RecentSaved,
    val isSortDialogVisible: Boolean = false,
    val errorMessage: String? = null,
) {
    val canImport: Boolean
        get() = selectedPlaceIds.isNotEmpty() && !isImporting
}

sealed interface SavedPlacePickerEvent {
    data class ShowMessage(val message: String) : SavedPlacePickerEvent
    data object Imported : SavedPlacePickerEvent
}

fun SavedPlaceSortType.displayName(): String =
    when (this) {
        SavedPlaceSortType.RecentSaved -> "최근 저장순"
        SavedPlaceSortType.OldestSaved -> "오래된 저장순"
        SavedPlaceSortType.Name -> "가나다순"
        SavedPlaceSortType.Popularity -> "인기순"
    }
