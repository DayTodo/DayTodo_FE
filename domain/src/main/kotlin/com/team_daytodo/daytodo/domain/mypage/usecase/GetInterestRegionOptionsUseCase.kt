package com.team_daytodo.daytodo.domain.mypage.usecase

import com.team_daytodo.daytodo.domain.mypage.model.InterestRegionOption
import javax.inject.Inject

// TODO: BE 지역 카탈로그(전체 지역 목록 + regionId) API가 완성되면 이 하드코딩을 제거하고
// 실제 API 응답으로 교체할 것. 요청은 이미 발송했고 응답 대기 중(2026-08-10 기준).
// 아래 regionId는 순차로 임의 부여한 가짜 값이라 실제 서버가 아는 regionId와 다를 수 있고,
// 이 값으로 PATCH /users/interest-regions를 호출해도 서버에 저장이 성공한다는 보장이 없다.
class GetInterestRegionOptionsUseCase @Inject constructor() {
    operator fun invoke(): List<InterestRegionOption> = interestRegionOptions

    private companion object {
        val interestRegionOptions: List<InterestRegionOption> = buildRegionOptions()

        fun buildRegionOptions(): List<InterestRegionOption> {
            var nextId = 1L
            val options = mutableListOf<InterestRegionOption>()

            fun addStandalone(name: String) {
                options += InterestRegionOption(nextId++, name, null)
            }

            fun addGroup(parentName: String, children: List<String>) {
                children.forEach { childName ->
                    options += InterestRegionOption(nextId++, childName, parentName)
                }
            }

            addStandalone("전국")
            addGroup(
                "서울",
                listOf(
                    "서울 전체",
                    "홍대/합정/망원/연남",
                    "강남/역삼/서초",
                    "성수/건대/왕십리",
                    "종로/을지로/동대문",
                    "여의도/영등포",
                    "잠실/송파/강동",
                    "용산/이태원/한남",
                ),
            )
            addGroup(
                "부산",
                listOf(
                    "부산 전체",
                    "해운대/센텀/송정",
                    "광안리/수영/민락",
                    "서면/전포/부전",
                    "남포/자갈치/영도",
                    "부산역/초량/동구",
                    "동래/온천장/명륜",
                    "기장/일광/정관",
                ),
            )
            addGroup(
                "제주",
                listOf(
                    "제주 전체",
                    "제주시/제주공항",
                    "애월/한림/협재",
                    "조천/함덕/김녕",
                    "성산/우도",
                    "서귀포/중문",
                    "표선/남원",
                ),
            )
            addGroup(
                "인천",
                listOf(
                    "인천 전체",
                    "송도/연수",
                    "구월/인천터미널",
                    "부평/부개",
                    "청라/서구",
                    "영종/인천공항",
                    "강화/석모도",
                ),
            )
            addGroup(
                "경기",
                listOf(
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
            )
            addGroup(
                "강원",
                listOf(
                    "강원 전체",
                    "춘천",
                    "원주",
                    "강릉",
                    "속초/고성",
                    "양양/동해",
                    "평창/대관령",
                ),
            )
            addGroup(
                "충청",
                listOf(
                    "충청 전체",
                    "대전",
                    "세종",
                    "청주",
                    "천안/아산",
                    "공주/부여",
                    "보령/태안",
                ),
            )
            addGroup(
                "경상",
                listOf(
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
            )
            addGroup(
                "전라",
                listOf(
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
            )

            return options
        }
    }
}
