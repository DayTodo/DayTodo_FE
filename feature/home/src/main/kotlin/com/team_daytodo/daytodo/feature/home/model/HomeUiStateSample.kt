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
        todayPickMagazines = listOf(
            HomeMagazineUiModel(
                placeId = "place-seoul-forest",
                title = "비 오는 날에도 걷기 좋은 실내 정원",
                location = "서울 강서구",
                description = "온실과 산책 동선을 함께 즐길 수 있는 서울 식물원 코스를 둘러보세요.",
                imageRes = R.drawable.ic_symbol,
            ),
            HomeMagazineUiModel(
                placeId = "place-gallery",
                title = "친구와 가볍게 보기 좋은 성수 전시",
                location = "서울 성동구",
                description = "전시 관람 후 성수 골목까지 이어지는 반나절 데이트 루트예요.",
                imageRes = R.drawable.ic_symbol,
            ),
            HomeMagazineUiModel(
                placeId = "place-bookshop",
                title = "취향을 나누기 좋은 독립서점",
                location = "서울 용산구",
                description = "작은 책과 사진집을 보며 오래 이야기하기 좋은 공간을 추천해요.",
                imageRes = R.drawable.ic_symbol,
            ),
        ),
    )
