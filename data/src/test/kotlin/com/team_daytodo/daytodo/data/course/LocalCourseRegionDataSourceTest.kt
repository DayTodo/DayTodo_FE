package com.team_daytodo.daytodo.data.course

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalCourseRegionDataSourceTest {
    private val dataSource = LocalCourseRegionDataSource()

    @Test
    fun `regions expose full display catalog with supported Seoul entries`() {
        val regions = dataSource.getRegions()

        assertEquals(9, regions.size)
        assertEquals("전국", regions.first().name)
        assertTrue(regions.first().children.isEmpty())

        val seoul = regions.first { it.name == "서울" }
        assertEquals(
            listOf(
                "서울 전체",
                "종로구",
                "중구",
                "용산구",
                "성동구",
                "광진구",
                "동대문구",
                "중랑구",
                "성북구",
                "강북구",
                "도봉구",
                "노원구",
                "은평구",
                "서대문구",
                "마포구",
                "양천구",
                "강서구",
                "구로구",
                "금천구",
                "영등포구",
                "동작구",
                "관악구",
                "서초구",
                "강남구",
                "송파구",
                "강동구",
            ),
            seoul.children,
        )
        assertTrue(regions.any { it.name == "부산" })
        assertTrue(regions.any { it.name == "경기" })
        assertEquals(
            listOf(
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
            regions.first { it.name == "부산" }.children,
        )
    }

    @Test
    fun `region names map to backend ids`() {
        assertEquals(1L, dataSource.getRegionId("서울 전체"))
        assertEquals(1L, dataSource.getRegionId("서울특별시"))
        assertEquals(2L, dataSource.getRegionId("종로구"))
        assertEquals(5L, dataSource.getRegionId("성동구"))
        assertEquals(24L, dataSource.getRegionId("강남구"))
        assertEquals(26L, dataSource.getRegionId("강동구"))
    }

    @Test
    fun `backend ids map to region names`() {
        assertEquals("서울 전체", dataSource.getRegionName(1L))
        assertEquals("종로구", dataSource.getRegionName(2L))
        assertEquals("성동구", dataSource.getRegionName(5L))
        assertEquals("강남구", dataSource.getRegionName(24L))
        assertEquals("강동구", dataSource.getRegionName(26L))
    }

    @Test
    fun `unknown regions are not mapped`() {
        assertNull(dataSource.getRegionId("성수/건대/왕십리"))
        assertNull(dataSource.getRegionId("부산 전체"))
        assertNull(dataSource.getRegionId("부산광역시"))
        assertNull(dataSource.getRegionName(999L))
        assertTrue(dataSource.getRegions().any { group -> group.name == "부산" })
    }
}
