package com.team_daytodo.daytodo.domain.magazine.usecase

import com.team_daytodo.daytodo.domain.magazine.model.MagazinePlace
import com.team_daytodo.daytodo.domain.magazine.model.SavedPlaceSortType
import com.team_daytodo.daytodo.domain.magazine.repository.MagazineRepository
import javax.inject.Inject

class GetSavedMagazinePlacesUseCase @Inject constructor(
    private val magazineRepository: MagazineRepository,
) {
    suspend operator fun invoke(
        sortType: SavedPlaceSortType = SavedPlaceSortType.RecentSaved,
    ): Result<List<MagazinePlace>> =
        magazineRepository.getSavedPlaces().map { places ->
            places.sortedBy(sortType)
        }
}

class GetMagazinePlaceDetailUseCase @Inject constructor(
    private val magazineRepository: MagazineRepository,
) {
    suspend operator fun invoke(placeId: String): Result<MagazinePlace> =
        magazineRepository.getMagazinePlace(placeId)
}

class ToggleMagazinePlaceSaveUseCase @Inject constructor(
    private val magazineRepository: MagazineRepository,
) {
    suspend operator fun invoke(placeId: String): Result<MagazinePlace> =
        magazineRepository.toggleSavedPlace(placeId)
}

private fun List<MagazinePlace>.sortedBy(
    sortType: SavedPlaceSortType,
): List<MagazinePlace> =
    when (sortType) {
        SavedPlaceSortType.RecentSaved -> sortedByDescending { it.savedAtMillis ?: Long.MIN_VALUE }
        SavedPlaceSortType.OldestSaved -> sortedBy { it.savedAtMillis ?: Long.MAX_VALUE }
        SavedPlaceSortType.Name -> sortedBy { it.name }
        SavedPlaceSortType.Popularity -> sortedByDescending(MagazinePlace::popularity)
    }
