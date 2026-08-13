package com.team_daytodo.daytodo.data.record

import com.team_daytodo.daytodo.core.model.CommonError
import com.team_daytodo.daytodo.data.api.RecordApi
import com.team_daytodo.daytodo.data.dto.record.WriteDiaryRequest
import com.team_daytodo.daytodo.data.dto.record.toDomain
import com.team_daytodo.daytodo.data.network.safeApiResult
import com.team_daytodo.daytodo.data.network.successOrThrow
import com.team_daytodo.daytodo.domain.record.model.RecordDiary
import com.team_daytodo.daytodo.domain.record.model.RecordPhoto
import com.team_daytodo.daytodo.domain.record.model.RecordPlace
import com.team_daytodo.daytodo.domain.record.model.RecordPlaceBookmark
import com.team_daytodo.daytodo.domain.record.repository.RecordRepository
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.serialization.json.Json

class RecordRepositoryImpl @Inject constructor(
    private val recordApi: RecordApi,
    private val json: Json,
) : RecordRepository {
    override suspend fun getCoursePlaces(courseId: Long): Result<List<RecordPlace>> =
        safeApiResult(json) {
            recordApi.getCoursePlaces(courseId)
        }.mapCatching { places ->
            places.map { it.toDomain() }
        }

    override suspend fun getMemoryPhotos(courseId: Long): Result<List<RecordPhoto>> =
        safeApiResult(json) {
            recordApi.getMemoryPhotos(courseId)
        }.mapCatching {
            it.toDomain()
        }

    override suspend fun getDiaryByDate(date: LocalDate): Result<RecordDiary?> = safeApiResult(json) {
        recordApi.getMemoryByDate(date)
    }.mapCatching {
        it.toDomain()
    }.recoverCatching { cause ->
        if (cause is CommonError.NotFound) null else throw cause
    }

    override suspend fun writeDiary(courseId: Long, content: String): Result<RecordDiary> =
        safeApiResult(json) {
            recordApi.writeDiary(WriteDiaryRequest(courseId, content))
        }.mapCatching {
            it.toDomain()
        }

    override suspend fun saveBookmark(placeId: Long): Result<RecordPlaceBookmark> =
        safeApiResult(json) {
            recordApi.saveBookmark(placeId)
        }.mapCatching {
            it.toDomain()
        }

    override suspend fun removeBookmark(bookmarkId: Long): Result<Unit> = safeApiResult(json) {
        recordApi.removeBookmark(bookmarkId).successOrThrow(json)
    }
}
