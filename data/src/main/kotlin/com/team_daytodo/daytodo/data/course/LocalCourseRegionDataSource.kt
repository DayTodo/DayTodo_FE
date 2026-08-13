package com.team_daytodo.daytodo.data.course

import com.team_daytodo.daytodo.domain.course.model.CourseRegionGroup
import javax.inject.Inject

class LocalCourseRegionDataSource @Inject constructor() {
    fun getRegions(): List<CourseRegionGroup> = displayRegionGroups

    fun getRegionId(regionName: String): Long? =
        regionIdByName[regionName.trim()]

    fun getRegionName(regionId: Long): String? =
        regionNameById[regionId]

    private companion object {
        val courseRegionCatalog = listOf(
            CourseRegionCatalogEntry(
                regionId = 1,
                regionName = "서울특별시",
                regionLevel = "SIDO",
                parentRegionId = null,
                parentRegionName = null,
            ),
            CourseRegionCatalogEntry(2, "종로구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(3, "중구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(4, "용산구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(5, "성동구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(6, "광진구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(7, "동대문구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(8, "중랑구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(9, "성북구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(10, "강북구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(11, "도봉구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(12, "노원구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(13, "은평구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(14, "서대문구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(15, "마포구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(16, "양천구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(17, "강서구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(18, "구로구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(19, "금천구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(20, "영등포구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(21, "동작구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(22, "관악구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(23, "서초구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(24, "강남구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(25, "송파구", "SIGUNGU", 1, "서울특별시"),
            CourseRegionCatalogEntry(26, "강동구", "SIGUNGU", 1, "서울특별시"),
        )

        val seoulDistricts = courseRegionCatalog
            .filter { it.parentRegionId == 1L }
            .map { it.regionName }

        val displayRegionGroups = listOf(
            CourseRegionGroup(
                name = "전국",
                children = emptyList(),
            ),
            CourseRegionGroup(
                name = "서울",
                children = listOf("서울 전체") + seoulDistricts,
            ),
            CourseRegionGroup(
                name = "부산",
                children = listOf(
                    "부산 전체",
                    "해운대/센텀시티/재송",
                    "광안리/수영/남천",
                    "서면/전포/부전",
                    "남포동/자갈치/광복동/영도",
                    "부산역/초량/동구",
                    "동래/온천장/명륜",
                    "연산/거제/시청",
                    "기장/송정/일광",
                    "사상/덕천/북구",
                    "명지/강서",
                    "하단/다대포/사하",
                ),
            ),
            CourseRegionGroup(
                name = "제주",
                children = listOf(
                    "제주 전체",
                    "제주시/제주공항/용담",
                    "노형/연동",
                    "애월/한림/협재",
                    "조천/함덕/김녕",
                    "구좌/월정리/세화",
                    "성산/우도",
                    "표선/남원",
                    "서귀포/천지연/정방",
                    "중문/색달",
                    "안덕/산방산",
                    "대정/모슬포",
                    "한라산/중산간",
                ),
            ),
            CourseRegionGroup(
                name = "인천",
                children = listOf(
                    "인천 전체",
                    "송도/연수",
                    "구월동/인천터미널/남동",
                    "부평/십정",
                    "청라/서구",
                    "검단/마전",
                    "계양/작전",
                    "인천역/차이나타운/신포동",
                    "월미도/동구",
                    "영종도/운서/인천공항",
                    "강화도/석모도",
                    "영흥도/덕적도/옹진",
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
                    "양양/낙산",
                    "동해/삼척",
                    "평창/대관령",
                    "정선/태백",
                    "홍천/횡성",
                    "영월",
                    "철원/화천",
                    "양구/인제",
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
                    "군포/의왕",
                    "하남/구리",
                    "남양주/가평",
                    "의정부/양주/동두천",
                    "포천/연천",
                    "화성/동탄/오산",
                    "평택/안성",
                    "안산/시흥",
                    "광주/이천/여주",
                    "양평",
                ),
            ),
            CourseRegionGroup(
                name = "경상",
                children = listOf(
                    "경상 전체",
                    "대구 동성로/반월당/중구",
                    "대구 동대구/수성구",
                    "대구 달서구/성서/앞산",
                    "경산/영천/청도",
                    "울산 삼산/남구",
                    "울산 중구/동구/북구",
                    "경주/보문/불국사",
                    "포항/영덕/울진",
                    "안동/영주/예천",
                    "문경/상주/김천",
                    "구미/칠곡",
                    "창원/마산/진해",
                    "김해/양산/밀양",
                    "진주/사천",
                    "거제/통영/고성",
                    "남해/하동",
                    "거창/함양/산청/합천",
                ),
            ),
            CourseRegionGroup(
                name = "전라",
                children = listOf(
                    "전라 전체",
                    "광주 충장로/동명동/양림동",
                    "광주 상무지구/서구",
                    "광주 첨단/수완/광산구",
                    "전주/완주",
                    "군산/익산",
                    "정읍/고창/부안",
                    "남원/임실/순창",
                    "무주/진안/장수",
                    "여수",
                    "순천/광양",
                    "목포/무안",
                    "신안/영암",
                    "나주/함평/영광",
                    "담양/곡성/구례",
                    "보성/화순",
                    "장흥/강진",
                    "해남/완도/진도",
                    "고흥",
                ),
            ),
        )

        val regionIdByName = courseRegionCatalog.associate { region ->
            region.regionName to region.regionId
        } + mapOf("서울 전체" to 1L)

        val regionNameById = courseRegionCatalog.associate { region ->
            region.regionId to region.regionName
        } + mapOf(1L to "서울 전체")
    }
}

private data class CourseRegionCatalogEntry(
    val regionId: Long,
    val regionName: String,
    val regionLevel: String,
    val parentRegionId: Long?,
    val parentRegionName: String?,
)
