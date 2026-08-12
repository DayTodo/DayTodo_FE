package com.team_daytodo.daytodo.data.record

import com.team_daytodo.daytodo.data.api.RecordApi
import com.team_daytodo.daytodo.data.dto.record.WriteDiaryRequest
import com.team_daytodo.daytodo.data.dto.record.toDomain
import com.team_daytodo.daytodo.domain.record.model.RecordDiary
import com.team_daytodo.daytodo.domain.record.model.RecordPhoto
import com.team_daytodo.daytodo.domain.record.model.RecordPlace
import com.team_daytodo.daytodo.domain.record.model.RecordPlaceBookmark
import com.team_daytodo.daytodo.domain.record.repository.RecordRepository
import java.time.LocalDate
import javax.inject.Inject
import retrofit2.HttpException
import retrofit2.Response

class RecordRepositoryImpl @Inject constructor(
    private val recordApi: RecordApi,
) : RecordRepository {
    override suspend fun getCoursePlaces(courseId: Long): Result<List<RecordPlace>> = runCatching {
        recordApi.getCoursePlaces(courseId).map { it.toDomain() }
    }

    override suspend fun getMemoryPhotos(courseId: Long): Result<List<RecordPhoto>> = runCatching {
        recordApi.getMemoryPhotos(courseId).toDomain()
    }

    override suspend fun getDiaryByDate(date: LocalDate): Result<RecordDiary?> = runCatching {
        recordApi.getMemoryByDate(date).toDomain()
    }.recoverCatching { cause ->
        if (cause is HttpException && cause.code() == 404) null else throw cause
    }

    override suspend fun writeDiary(courseId: Long, content: String): Result<RecordDiary> = runCatching {
        recordApi.writeDiary(WriteDiaryRequest(courseId, content)).toDomain()
    }

    override suspend fun saveBookmark(placeId: Long): Result<RecordPlaceBookmark> = runCatching {
        recordApi.saveBookmark(placeId).toDomain()
    }

    override suspend fun removeBookmark(bookmarkId: Long): Result<Unit> = runCatching {
        recordApi.removeBookmark(bookmarkId).throwIfNotSuccessful()
    }

    private fun Response<Unit>.throwIfNotSuccessful() {
        if (!isSuccessful) throw HttpException(this)
    }
}
