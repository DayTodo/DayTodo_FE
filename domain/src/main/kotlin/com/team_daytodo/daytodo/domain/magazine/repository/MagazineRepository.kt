package com.team_daytodo.daytodo.domain.magazine.repository

import com.team_daytodo.daytodo.domain.magazine.model.MagazinePlace

interface MagazineRepository {
    suspend fun getSavedPlaces(): Result<List<MagazinePlace>>

    suspend fun getMagazinePlace(placeId: String): Result<MagazinePlace>

    suspend fun toggleSavedPlace(placeId: String): Result<MagazinePlace>
}
