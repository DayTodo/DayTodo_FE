package com.team_daytodo.daytodo.data.record

import com.team_daytodo.daytodo.data.api.RecordApi
import com.team_daytodo.daytodo.data.dto.record.WriteDiaryRequest
import com.team_daytodo.daytodo.data.dto.record.toDomain
import com.team_daytodo.daytodo.core.model.CommonError
import com.team_daytodo.daytodo.data.network.safeApiCall
import com.team_daytodo.daytodo.domain.record.model.RecordDiary
import com.team_daytodo.daytodo.domain.record.model.RecordPhoto
import com.team_daytodo.daytodo.domain.record.model.RecordPlace
import com.team_daytodo.daytodo.domain.record.model.RecordPlaceBookmark
import com.team_daytodo.daytodo.domain.record.repository.RecordRepository
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import retrofit2.Response

class RecordRepositoryImpl @Inject constructor(
    private val recordApi: RecordApi,
    private val json: Json,
) : RecordRepository {
    // safeApiCall로 BE 에러 응답 바디의 실제 message(예: "완료된 코스에만 일기를 작성할 수
    // 있습니다.")를 파싱해 예외에 실어 보낸다. 이전에는 runCatching만 써서 HttpException의
    // 기본 message("HTTP 400 Bad Request" 등)가 그대로 노출되어, 실패 사유를 알 수 없는
    // 토스트만 떴다.
    override suspend fun getCoursePlaces(courseId: Long): Result<List<RecordPlace>> = runCatching {
        safeApiCall(json) { recordApi.getCoursePlaces(courseId) }.map { it.toDomain() }
    }

    override suspend fun getMemoryPhotos(courseId: Long): Result<List<RecordPhoto>> = runCatching {
        safeApiCall(json) { recordApi.getMemoryPhotos(courseId) }.toDomain()
    }

    override suspend fun getDiaryByDate(date: LocalDate): Result<RecordDiary?> = runCatching {
        safeApiCall(json) { recordApi.getMemoryByDate(date) }.toDomain()
    }.recoverCatching { cause ->
        // safeApiCall이 404 HttpException을 CommonError.NotFound로 변환하므로 이 타입으로 잡는다.
        if (cause is CommonError.NotFound) null else throw cause
    }

    override suspend fun writeDiary(courseId: Long, content: String): Result<RecordDiary> = runCatching {
        safeApiCall(json) { recordApi.writeDiary(WriteDiaryRequest(courseId, content)) }.toDomain()
    }

    override suspend fun saveBookmark(placeId: Long): Result<RecordPlaceBookmark> = runCatching {
        safeApiCall(json) { recordApi.saveBookmark(placeId) }.toDomain()
    }

    override suspend fun removeBookmark(bookmarkId: Long): Result<Unit> = runCatching {
        safeApiCall(json) { recordApi.removeBookmark(bookmarkId).throwIfNotSuccessful() }
    }

    private fun Response<Unit>.throwIfNotSuccessful() {
        if (!isSuccessful) throw HttpException(this)
    }
}
