package com.team_daytodo.daytodo.feature.today.state

import com.team_daytodo.daytodo.domain.today.model.MemoryPhoto
import com.team_daytodo.daytodo.feature.today.model.CourseMember
import com.team_daytodo.daytodo.feature.today.model.CoursePlace

data class TodayUiState(
    val hasCourse: Boolean = false,
    val courseId: Long? = null,
    val courseName: String? = null,
    val members: List<CourseMember> = emptyList(),
    val places: List<CoursePlace> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    // 업로드가 완료되어 서버에 저장된 추억 사진(선택 즉시 업로드되므로 별도 "저장" 상태가 없다)
    val memoryPhotos: List<MemoryPhoto> = emptyList(),
    // 방금 선택되어 업로드가 진행 중인 로컬 사진 Uri(그리드에 로딩 스피너로 표시)
    val pendingMemoryPhotoUris: List<String> = emptyList(),
)
