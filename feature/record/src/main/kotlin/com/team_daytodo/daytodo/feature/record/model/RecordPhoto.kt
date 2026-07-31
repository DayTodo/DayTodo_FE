package com.team_daytodo.daytodo.feature.record.model

data class RecordPhoto(
    val id: String,
    // 서버/DB 이미지로 교체될 자리. 지금은 dummypicture 리소스, 나중엔 URL 기반 로더로 대체 가능.
    val imageRes: Int? = null,
)
