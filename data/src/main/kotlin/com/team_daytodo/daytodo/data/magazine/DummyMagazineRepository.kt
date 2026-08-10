package com.team_daytodo.daytodo.data.magazine

import com.team_daytodo.daytodo.domain.magazine.model.MagazinePlace
import com.team_daytodo.daytodo.domain.magazine.model.MagazinePlaceNotFoundException
import com.team_daytodo.daytodo.domain.magazine.repository.MagazineRepository
import javax.inject.Inject
import kotlinx.coroutines.delay

class DummyMagazineRepository @Inject constructor() : MagazineRepository {
    private val createdAtMillis = System.currentTimeMillis()
    private val savedAtByPlaceId = mutableMapOf(
        "place-seoul-forest" to createdAtMillis - 1_000L * 60L * 8L,
        "place-seongsu-cafe" to createdAtMillis - 1_000L * 60L * 55L,
        "place-gallery" to createdAtMillis - 1_000L * 60L * 60L * 7L,
        "place-bookshop" to createdAtMillis - 1_000L * 60L * 60L * 28L,
        "place-hanriver" to createdAtMillis - 1_000L * 60L * 60L * 72L,
    )

    private val places = listOf(
        StoredMagazinePlace(
            id = "place-seoul-forest",
            name = "서울 식물원",
            region = "서울 강서구",
            address = "서울 강서구 마곡동로 161",
            category = "식물원, 수목원",
            categoryPath = listOf("나들이", "공원", "식물원"),
            description = "넓은 온실과 계절 정원을 함께 볼 수 있는 산책형 장소예요. 실내 동선이 넓어 날씨가 애매한 날에도 부담 없이 머물 수 있고, 주변 카페나 식당과 이어 코스로 구성하기 좋아요.",
            phoneNumber = "02-2104-9716",
            popularity = 98,
        ),
        StoredMagazinePlace(
            id = "place-seongsu-cafe",
            name = "로우키 성수",
            region = "서울 성동구",
            address = "서울 성동구 서울숲2길 22",
            category = "카페",
            categoryPath = listOf("카페", "스페셜티", "커피"),
            description = "차분한 분위기에서 커피를 고르기 좋은 성수 카페예요. 테이블 간격이 여유로운 편이라 데이트나 친구와의 짧은 만남에 잘 맞습니다.",
            phoneNumber = "02-0000-1207",
            popularity = 91,
        ),
        StoredMagazinePlace(
            id = "place-gallery",
            name = "그라운드시소 성수",
            region = "서울 성동구",
            address = "서울 성동구 아차산로17길 49",
            category = "전시",
            categoryPath = listOf("문화생활", "전시", "미디어아트"),
            description = "사진과 영상 중심의 전시가 자주 열리는 공간이에요. 관람 후 성수 골목을 걷거나 식사 장소로 이동하기 좋은 위치라 반나절 코스로 묶기 좋습니다.",
            phoneNumber = "02-1522-1796",
            popularity = 87,
        ),
        StoredMagazinePlace(
            id = "place-bookshop",
            name = "스토리지북앤필름",
            region = "서울 용산구",
            address = "서울 용산구 신흥로 115-1",
            category = "서점",
            categoryPath = listOf("문화생활", "독립서점", "라이프스타일"),
            description = "작은 독립 출판물과 사진집을 천천히 둘러보기 좋은 서점이에요. 가벼운 산책 코스 사이에 넣으면 취향 이야기를 나누기 좋습니다.",
            phoneNumber = "02-797-9965",
            popularity = 83,
        ),
        StoredMagazinePlace(
            id = "place-hanriver",
            name = "반포한강공원",
            region = "서울 서초구",
            address = "서울 서초구 신반포로11길 40",
            category = "야외",
            categoryPath = listOf("나들이", "한강", "야경"),
            description = "해질 무렵 산책과 야경을 함께 즐기기 좋은 공원이에요. 날씨가 좋은 날에는 코스의 마지막 장소로 두면 하루를 편하게 마무리할 수 있어요.",
            phoneNumber = "02-3780-0541",
            popularity = 95,
        ),
        StoredMagazinePlace(
            id = "place-dinner",
            name = "소품 한남",
            region = "서울 용산구",
            address = "서울 용산구 이태원로54길 58",
            category = "한식",
            categoryPath = listOf("맛집", "한식", "다이닝"),
            description = "깔끔한 한식 메뉴와 조용한 분위기가 어울리는 식당이에요. 기념일이나 차분한 저녁 코스에 넣기 좋습니다.",
            phoneNumber = "02-0000-2800",
            popularity = 88,
        ),
        StoredMagazinePlace(
            id = "place-seongsu-lunch",
            name = "오브젝트 성수",
            region = "서울 성동구",
            address = "서울 성동구 연무장길 37",
            category = "소품샵",
            categoryPath = listOf("쇼핑", "소품샵", "라이프스타일"),
            description = "가볍게 둘러보기 좋은 소품과 문구가 많은 성수 편집숍이에요. 카페나 전시 전후로 짧게 들르기 좋습니다.",
            phoneNumber = "02-0000-1800",
            popularity = 78,
        ),
    )

    private val placeById = places.associateBy(StoredMagazinePlace::id)

    override suspend fun getSavedPlaces(): Result<List<MagazinePlace>> =
        runCatching {
            delay(QueryDelayMillis)
            places
                .filter { place -> place.id in savedAtByPlaceId }
                .map { place -> place.toDomain() }
        }

    override suspend fun getMagazinePlace(placeId: String): Result<MagazinePlace> =
        runCatching {
            delay(QueryDelayMillis)
            requirePlace(placeId).toDomain()
        }

    override suspend fun toggleSavedPlace(placeId: String): Result<MagazinePlace> =
        runCatching {
            delay(ActionDelayMillis)
            val place = requirePlace(placeId)
            if (placeId in savedAtByPlaceId) {
                savedAtByPlaceId.remove(placeId)
            } else {
                savedAtByPlaceId[placeId] = System.currentTimeMillis()
            }
            place.toDomain()
        }

    private fun requirePlace(placeId: String): StoredMagazinePlace =
        placeById[placeId] ?: throw MagazinePlaceNotFoundException(placeId)

    private fun StoredMagazinePlace.toDomain(): MagazinePlace =
        MagazinePlace(
            id = id,
            name = name,
            region = region,
            address = address,
            category = category,
            categoryPath = categoryPath,
            description = description,
            phoneNumber = phoneNumber,
            imageUrl = imageUrl,
            savedAtMillis = savedAtByPlaceId[id],
            popularity = popularity,
            isSaved = id in savedAtByPlaceId,
        )

    private data class StoredMagazinePlace(
        val id: String,
        val name: String,
        val region: String,
        val address: String,
        val category: String,
        val categoryPath: List<String>,
        val description: String,
        val phoneNumber: String,
        val popularity: Int,
        val imageUrl: String? = null,
    )

    private companion object {
        const val QueryDelayMillis = 120L
        const val ActionDelayMillis = 100L
    }
}
