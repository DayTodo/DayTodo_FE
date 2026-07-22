package com.team_daytodo.daytodo.data.course

import com.team_daytodo.daytodo.domain.course.model.CourseRegionGroup
import javax.inject.Inject

class LocalCourseRegionDataSource @Inject constructor() {
    fun getRegions(): List<CourseRegionGroup> = courseRegions

    private companion object {
        val courseRegions = listOf(
            CourseRegionGroup(
                name = "전국",
                children = emptyList(),
            ),
            CourseRegionGroup(
                name = "서울",
                children = listOf(
                    "서울 전체",
                    "홍대/합정/망원/연남",
                    "강남/역삼/서초",
                    "성수/건대/왕십리",
                    "종로/을지로/동대문",
                    "여의도/영등포",
                    "잠실/송파/강동",
                    "용산/이태원/한남",
                ),
            ),
            CourseRegionGroup(
                name = "부산",
                children = listOf(
                    "부산 전체",
                    "해운대/센텀/송정",
                    "광안리/수영/민락",
                    "서면/전포/부전",
                    "남포/자갈치/영도",
                    "부산역/초량/동구",
                    "동래/온천장/명륜",
                    "기장/일광/정관",
                ),
            ),
            CourseRegionGroup(
                name = "제주",
                children = listOf(
                    "제주 전체",
                    "제주시/제주공항",
                    "애월/한림/협재",
                    "조천/함덕/김녕",
                    "성산/우도",
                    "서귀포/중문",
                    "표선/남원",
                ),
            ),
            CourseRegionGroup(
                name = "인천",
                children = listOf(
                    "인천 전체",
                    "송도/연수",
                    "구월/인천터미널",
                    "부평/부개",
                    "청라/서구",
                    "영종/인천공항",
                    "강화/석모도",
                ),
            ),
            CourseRegionGroup(
                name = "경기",
                children = listOf(
                    "경기 전체",
                    "수원/광교",
                    "성남/분당/판교",
                    "용인/수지/기흥",
                    "고양/일산",
                    "김포/파주",
                    "부천/광명",
                    "안양/과천",
                    "하남/구리",
                    "의정부/양주/동두천",
                ),
            ),
            CourseRegionGroup(
                name = "강원",
                children = listOf(
                    "강원 전체",
                    "춘천",
                    "원주",
                    "강릉",
                    "속초/고성",
                    "양양/동해",
                    "평창/대관령",
                ),
            ),
            CourseRegionGroup(
                name = "충청",
                children = listOf(
                    "충청 전체",
                    "대전",
                    "세종",
                    "청주",
                    "천안/아산",
                    "공주/부여",
                    "보령/태안",
                ),
            ),
            CourseRegionGroup(
                name = "경상",
                children = listOf(
                    "경상 전체",
                    "대구",
                    "울산",
                    "경주",
                    "포항",
                    "안동",
                    "창원/마산/진해",
                    "진주/사천",
                    "거제/통영",
                ),
            ),
            CourseRegionGroup(
                name = "전라",
                children = listOf(
                    "전라 전체",
                    "광주",
                    "전주",
                    "군산/익산",
                    "여수",
                    "순천/광양",
                    "목포/무안",
                    "담양/곡성/구례",
                    "해남/완도/진도",
                ),
            ),
        )
    }
}
