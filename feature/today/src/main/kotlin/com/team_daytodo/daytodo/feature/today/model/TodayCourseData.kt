package com.team_daytodo.daytodo.feature.today.model

data class TodayCourseData(
    val hasCourse: Boolean,
    val members: List<CourseMember>,
    val places: List<CoursePlace>,
)

// 실제 데이터 소스(API/ViewModel)가 붙기 전까지 사용하는 today feature 내부 더미데이터
internal fun sampleTodayCourseData(): TodayCourseData =
    TodayCourseData(
        hasCourse = true,
        members = listOf(
            CourseMember(id = "1", name = "나"),
            CourseMember(id = "2", name = "수아"),
            CourseMember(id = "3", name = "민지"),
        ),
        places = listOf(
            CoursePlace(id = "1", name = "성수 카페거리", category = "카페"),
            CoursePlace(id = "2", name = "서울숲", category = "공원"),
            CoursePlace(id = "3", name = "레스토랑 예약", category = "맛집"),
        ),
    )
