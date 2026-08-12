package com.team_daytodo.daytodo.feature.home.model

import com.team_daytodo.daytodo.core.model.Relationship
import com.team_daytodo.daytodo.uikit.R

internal fun sampleHomeUiState(): HomeUiState =
    HomeUiState(
        username = "바니",
        interestLocation = "서울 전체",
        todayCourse =
//            null,
            TodayCourse(
                date = "2026.7.9. (목)",
                title = "성수 감성 산책",
                relationship = Relationship.FRIEND,
                members = listOf(
                    CourseMember(name = "민지", profileImage = null),
                    CourseMember(name = "서연", profileImage = R.drawable.ic_symbol),
                    CourseMember(name = "하윤", profileImage = R.drawable.ic_symbol),
            ),
        ),
        upcomingCourse =
//            null,
            UpcomingCourse(
                relationship = Relationship.FRIEND,
                date = "2026.7.12. (일)",
            ),
        createdCourses =
//            listOf(),
            listOf(
                CreatedCourse(
                    title = "한강 피크닉",
                    date = "2026년 7월 12일 일요일",
                    memberCount = 4,
                    dDay = "D-3",
                    relationship = Relationship.FRIEND,
                ),
                CreatedCourse(
                    title = "기념일 디너",
                    date = "2026년 7월 18일 토요일",
                    memberCount = 2,
                    dDay = "D-9",
                    relationship = Relationship.LOVER,
                ),
                CreatedCourse(
                    title = "가족 캠핑",
                    date = "2026년 8월 2일 일요일",
                    memberCount = 5,
                    dDay = "D-24",
                    relationship = Relationship.FAMILY,
                ),
            ),
        todayPickMagazines = emptyList(),
    )
