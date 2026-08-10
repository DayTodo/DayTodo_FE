package com.team_daytodo.daytodo.data.course

import com.team_daytodo.daytodo.core.model.Relationship
import com.team_daytodo.daytodo.domain.course.model.CourseComment
import com.team_daytodo.daytodo.domain.course.model.CourseCommentThread
import com.team_daytodo.daytodo.domain.course.model.CourseCoordinate
import com.team_daytodo.daytodo.domain.course.model.CourseCreateRequest
import com.team_daytodo.daytodo.domain.course.model.CourseCreateResult
import com.team_daytodo.daytodo.domain.course.model.CourseDate
import com.team_daytodo.daytodo.domain.course.model.CourseDetail
import com.team_daytodo.daytodo.domain.course.model.CourseMember
import com.team_daytodo.daytodo.domain.course.model.CoursePlace
import com.team_daytodo.daytodo.domain.course.model.CoursePlaceRecommendation
import com.team_daytodo.daytodo.domain.course.model.CoursePlaceSearchResult
import com.team_daytodo.daytodo.domain.course.model.CourseRegionGroup
import com.team_daytodo.daytodo.domain.course.model.CourseRegionLoadException
import com.team_daytodo.daytodo.domain.course.model.CourseRoomCreateException
import com.team_daytodo.daytodo.domain.course.model.CourseSummary
import com.team_daytodo.daytodo.domain.course.model.CourseUpdateRequest
import com.team_daytodo.daytodo.domain.course.model.PlaceRecommender
import com.team_daytodo.daytodo.domain.course.repository.CourseRepository
import javax.inject.Inject
import kotlinx.coroutines.delay

class DummyCourseRepository @Inject constructor(
    private val localCourseRegionDataSource: LocalCourseRegionDataSource,
) : CourseRepository {
    private val currentMember = CourseMember(id = "member-me", name = "나")
    private val minji = CourseMember(id = "member-minji", name = "민지")
    private val seoyeon = CourseMember(id = "member-seoyeon", name = "서연")
    private val family = CourseMember(id = "member-family", name = "엄마")

    private val placeCatalog = listOf(
        CoursePlace(
            id = "place-seongsu-lunch",
            name = "오브젝트 성수",
            address = "서울 성동구 연무장길",
            category = "편집샵",
            description = "가볍게 둘러보기 좋은 소품과 문구가 많은 성수 산책 시작점이에요.",
            expectedPrice = 18000,
            coordinate = CourseCoordinate(latitude = 37.5444, longitude = 127.0557),
        ),
        CoursePlace(
            id = "place-seongsu-cafe",
            name = "로우키 성수",
            address = "서울 성동구 서울숲2길",
            category = "카페",
            description = "대화하기 좋은 좌석과 부드러운 커피가 있는 카페예요.",
            expectedPrice = 12000,
            coordinate = CourseCoordinate(latitude = 37.5468, longitude = 127.0428),
        ),
        CoursePlace(
            id = "place-seoul-forest",
            name = "서울숲",
            address = "서울 성동구 뚝섬로",
            category = "공원",
            description = "코스 중간에 걷기 좋은 넓은 녹지 공간이에요.",
            expectedPrice = 0,
            coordinate = CourseCoordinate(latitude = 37.5446, longitude = 127.0372),
        ),
        CoursePlace(
            id = "place-gallery",
            name = "디뮤지엄 성수",
            address = "서울 성동구 왕십리로",
            category = "전시",
            description = "사진으로 남기기 좋은 전시가 자주 열리는 실내 장소예요.",
            expectedPrice = 17000,
            coordinate = CourseCoordinate(latitude = 37.5432, longitude = 127.0446),
        ),
        CoursePlace(
            id = "place-dinner",
            name = "난포 한남",
            address = "서울 용산구 이태원로",
            category = "한식",
            description = "깔끔한 한식 메뉴로 오래 앉아 이야기하기 좋은 식당이에요.",
            expectedPrice = 28000,
            coordinate = CourseCoordinate(latitude = 37.5364, longitude = 127.0008),
        ),
        CoursePlace(
            id = "place-bookshop",
            name = "스토리지북앤필름",
            address = "서울 용산구 신흥로",
            category = "서점",
            description = "작은 독립 출판물을 구경하며 취향 이야기를 나누기 좋아요.",
            expectedPrice = 15000,
            coordinate = CourseCoordinate(latitude = 37.5439, longitude = 126.9875),
        ),
        CoursePlace(
            id = "place-hanriver",
            name = "반포한강공원",
            address = "서울 서초구 신반포로",
            category = "야외",
            description = "저녁 산책과 야경을 함께 즐길 수 있는 넓은 강변 코스예요.",
            expectedPrice = 0,
            coordinate = CourseCoordinate(latitude = 37.5106, longitude = 126.9946),
        ),
    )
    private val placeById = placeCatalog.associateBy(CoursePlace::id)

    private val courseRooms = mutableMapOf(
        "course-seongsu" to StoredCourseRoom(
            id = "course-seongsu",
            name = "성수 감성 산책",
            region = "성수/건대/왕십리",
            regionCoordinate = CourseCoordinate(latitude = 37.5446, longitude = 127.0557),
            date = CourseDate(year = 2026, month = 7, day = 27),
            minBudget = 10000,
            maxBudget = 35000,
            relationship = Relationship.FRIEND,
            members = listOf(currentMember, minji, seoyeon),
            recommendations = mutableListOf(
                StoredRecommendation(
                    placeId = "place-seongsu-lunch",
                    recommender = PlaceRecommender.Ai,
                    likedByMemberIds = mutableSetOf("member-minji"),
                ),
                StoredRecommendation(
                    placeId = "place-seongsu-cafe",
                    recommender = PlaceRecommender.Member("member-minji", "민지"),
                    likedByMemberIds = mutableSetOf("member-me", "member-seoyeon"),
                ),
                StoredRecommendation(
                    placeId = "place-seoul-forest",
                    recommender = PlaceRecommender.Member("member-seoyeon", "서연"),
                    likedByMemberIds = mutableSetOf(),
                ),
            ),
            coursePlaceIds = mutableListOf("place-seongsu-cafe", "place-seoul-forest"),
            commentsByPlaceId = mutableMapOf(
                "place-seongsu-cafe" to mutableListOf(
                    CourseComment(
                        id = "comment-1",
                        author = minji,
                        content = "여기 라떼가 진짜 좋아요.",
                        createdAtMillis = System.currentTimeMillis() - 1000L * 60L * 16L,
                    ),
                    CourseComment(
                        id = "comment-2",
                        author = seoyeon,
                        content = "서울숲 가기 전에 들르면 동선이 좋아요.",
                        createdAtMillis = System.currentTimeMillis() - 1000L * 60L * 60L * 2L,
                    ),
                ),
            ),
        ),
        "course-hannam" to StoredCourseRoom(
            id = "course-hannam",
            name = "기념일 한남 코스",
            region = "용산/이태원/한남",
            regionCoordinate = CourseCoordinate(latitude = 37.5350, longitude = 127.0018),
            date = CourseDate(year = 2026, month = 8, day = 3),
            minBudget = 20000,
            maxBudget = 60000,
            relationship = Relationship.LOVER,
            members = listOf(currentMember, CourseMember(id = "member-lover", name = "지우")),
            recommendations = mutableListOf(
                StoredRecommendation(
                    placeId = "place-dinner",
                    recommender = PlaceRecommender.Ai,
                    likedByMemberIds = mutableSetOf("member-me"),
                ),
                StoredRecommendation(
                    placeId = "place-bookshop",
                    recommender = PlaceRecommender.Member("member-lover", "지우"),
                    likedByMemberIds = mutableSetOf(),
                ),
            ),
            coursePlaceIds = mutableListOf("place-bookshop", "place-dinner"),
            commentsByPlaceId = mutableMapOf(),
        ),
        "course-family" to StoredCourseRoom(
            id = "course-family",
            name = "가족 한강 나들이",
            region = "여의도/영등포",
            regionCoordinate = CourseCoordinate(latitude = 37.5259, longitude = 126.9256),
            date = CourseDate(year = 2026, month = 8, day = 16),
            minBudget = 0,
            maxBudget = 45000,
            relationship = Relationship.FAMILY,
            members = listOf(currentMember, family),
            recommendations = mutableListOf(
                StoredRecommendation(
                    placeId = "place-hanriver",
                    recommender = PlaceRecommender.Ai,
                    likedByMemberIds = mutableSetOf("member-family"),
                ),
            ),
            coursePlaceIds = mutableListOf("place-hanriver"),
            commentsByPlaceId = mutableMapOf(),
        ),
    )

    override suspend fun getCourseRegions(): Result<List<CourseRegionGroup>> =
        runCatching {
            localCourseRegionDataSource.getRegions()
        }.recoverCatching { cause ->
            throw CourseRegionLoadException(cause)
        }

    override suspend fun createCourseRoom(
        request: CourseCreateRequest,
    ): Result<CourseCreateResult> =
        runCatching {
            delay(CourseCreateDelayMillis)

            val courseId = buildCourseId(request.roomName)
            courseRooms[courseId] = StoredCourseRoom(
                id = courseId,
                name = request.roomName.trim(),
                region = request.region,
                regionCoordinate = request.region.toRegionCoordinate(),
                date = request.date,
                minBudget = request.minBudget,
                maxBudget = request.maxBudget,
                relationship = request.relationship,
                members = listOf(currentMember),
                recommendations = mutableListOf(),
                coursePlaceIds = mutableListOf(),
                commentsByPlaceId = mutableMapOf(),
            )

            CourseCreateResult(
                inviteLink = "$InviteBaseUrl/$courseId",
            )
        }.recoverCatching { cause ->
            throw CourseRoomCreateException(cause)
        }

    override suspend fun joinCourse(inviteCode: String): Result<String> =
        runCatching {
            delay(ActionDelayMillis)
            "Joined course"
        }

    override suspend fun getUpcomingCourses(): Result<List<CourseSummary>> =
        runCatching {
            delay(QueryDelayMillis)
            courseRooms.values
                .sortedBy { it.date.sortKey }
                .map { it.toSummary() }
        }

    override suspend fun getCourseDetail(courseId: String): Result<CourseDetail> =
        runCatching {
            delay(QueryDelayMillis)
            requireRoom(courseId).toDetail()
        }

    override suspend fun searchPlaces(
        courseId: String,
        query: String,
    ): Result<List<CoursePlaceSearchResult>> =
        runCatching {
            delay(QueryDelayMillis)
            val room = requireRoom(courseId)
            val normalizedQuery = query.trim()
            if (normalizedQuery.contains("없음") || normalizedQuery.contains("zz")) {
                return@runCatching emptyList()
            }

            placeCatalog
                .filter { place ->
                    normalizedQuery in place.name ||
                        normalizedQuery in place.address ||
                        normalizedQuery in place.category ||
                        normalizedQuery in place.description
                }
                .map { place ->
                    CoursePlaceSearchResult(
                        place = place,
                        recommendedByCurrentMember = room.recommendations.any {
                            it.placeId == place.id && it.recommender.isCurrentMember()
                        },
                        isInCourse = place.id in room.coursePlaceIds,
                    )
                }
        }

    override suspend fun togglePlaceLike(
        courseId: String,
        placeId: String,
    ): Result<CourseDetail> =
        runCatching {
            delay(ActionDelayMillis)
            val room = requireRoom(courseId)
            val recommendation = room.recommendations.firstOrNull { it.placeId == placeId }
                ?: throw NoSuchElementException("추천 장소를 찾을 수 없어요.")

            if (currentMember.id in recommendation.likedByMemberIds) {
                recommendation.likedByMemberIds.remove(currentMember.id)
            } else {
                recommendation.likedByMemberIds.add(currentMember.id)
            }
            room.toDetail()
        }

    override suspend fun recommendPlace(
        courseId: String,
        placeId: String,
    ): Result<CourseDetail> =
        runCatching {
            delay(ActionDelayMillis)
            val room = requireRoom(courseId)
            requirePlace(placeId)
            room.ensureCurrentMemberRecommendation(placeId)
            room.toDetail()
        }

    override suspend fun toggleCoursePlace(
        courseId: String,
        placeId: String,
    ): Result<CourseDetail> =
        runCatching {
            delay(ActionDelayMillis)
            val room = requireRoom(courseId)
            requirePlace(placeId)
            room.ensureCurrentMemberRecommendation(placeId)

            if (placeId in room.coursePlaceIds) {
                room.coursePlaceIds.remove(placeId)
            } else {
                room.coursePlaceIds.add(placeId)
            }
            room.toDetail()
        }

    override suspend fun removeCoursePlace(
        courseId: String,
        placeId: String,
    ): Result<CourseDetail> =
        runCatching {
            delay(ActionDelayMillis)
            val room = requireRoom(courseId)
            room.coursePlaceIds.remove(placeId)
            room.toDetail()
        }

    override suspend fun moveCoursePlace(
        courseId: String,
        fromIndex: Int,
        toIndex: Int,
    ): Result<CourseDetail> =
        runCatching {
            delay(ActionDelayMillis)
            val room = requireRoom(courseId)
            if (fromIndex !in room.coursePlaceIds.indices || toIndex !in room.coursePlaceIds.indices) {
                return@runCatching room.toDetail()
            }

            val movingPlaceId = room.coursePlaceIds.removeAt(fromIndex)
            room.coursePlaceIds.add(toIndex, movingPlaceId)
            room.toDetail()
        }

    override suspend fun updateCourseSettings(request: CourseUpdateRequest): Result<CourseDetail> =
        runCatching {
            delay(ActionDelayMillis)
            val room = requireRoom(request.courseId)
            room.name = request.name.trim()
            room.region = request.region
            room.regionCoordinate = request.region.toRegionCoordinate()
            room.date = request.date
            room.minBudget = request.minBudget
            room.maxBudget = request.maxBudget
            room.toDetail()
        }

    override suspend fun getPlaceComments(
        courseId: String,
        placeId: String,
    ): Result<CourseCommentThread> =
        runCatching {
            delay(QueryDelayMillis)
            val room = requireRoom(courseId)
            CourseCommentThread(
                place = requirePlace(placeId),
                comments = room.commentsByPlaceId[placeId].orEmpty().toList(),
            )
        }

    override suspend fun addPlaceComment(
        courseId: String,
        placeId: String,
        content: String,
    ): Result<CourseCommentThread> =
        runCatching {
            delay(ActionDelayMillis)
            val room = requireRoom(courseId)
            val comments = room.commentsByPlaceId.getOrPut(placeId) { mutableListOf() }
            comments.add(
                CourseComment(
                    id = "comment-${System.currentTimeMillis()}",
                    author = currentMember,
                    content = content.trim(),
                    createdAtMillis = System.currentTimeMillis(),
                ),
            )

            CourseCommentThread(
                place = requirePlace(placeId),
                comments = comments.toList(),
            )
        }

    private fun requireRoom(courseId: String): StoredCourseRoom =
        courseRooms[courseId] ?: throw NoSuchElementException("코스 정보를 찾을 수 없어요.")

    private fun requirePlace(placeId: String): CoursePlace =
        placeById[placeId] ?: throw NoSuchElementException("장소 정보를 찾을 수 없어요.")

    private fun buildCourseId(roomName: String): String {
        val roomHash = roomName
            .trim()
            .hashCode()
            .toUInt()
            .toString(radix = InviteHashRadix)

        return "course-$roomHash"
    }

    private fun StoredCourseRoom.ensureCurrentMemberRecommendation(placeId: String) {
        val alreadyRecommended = recommendations.any {
            it.placeId == placeId && it.recommender.isCurrentMember()
        }
        if (!alreadyRecommended) {
            recommendations.add(
                StoredRecommendation(
                    placeId = placeId,
                    recommender = PlaceRecommender.Member(currentMember.id, currentMember.name),
                    likedByMemberIds = mutableSetOf(currentMember.id),
                ),
            )
        }
    }

    private fun StoredCourseRoom.toSummary(): CourseSummary =
        CourseSummary(
            id = id,
            name = name,
            region = region,
            date = date,
            relationship = relationship,
            members = members,
            placeCount = coursePlaceIds.size,
        )

    private fun StoredCourseRoom.toDetail(): CourseDetail =
        CourseDetail(
            id = id,
            name = name,
            region = region,
            regionCoordinate = regionCoordinate,
            date = date,
            minBudget = minBudget,
            maxBudget = maxBudget,
            relationship = relationship,
            currentMemberId = currentMember.id,
            members = members,
            recommendedPlaces = recommendations.mapNotNull { recommendation ->
                val place = placeById[recommendation.placeId] ?: return@mapNotNull null
                CoursePlaceRecommendation(
                    place = place,
                    recommender = recommendation.recommender,
                    likedByMemberIds = recommendation.likedByMemberIds.toSet(),
                    commentCount = commentsByPlaceId[recommendation.placeId]?.size ?: 0,
                )
            },
            coursePlaces = coursePlaceIds.mapNotNull(placeById::get),
        )

    private fun PlaceRecommender.isCurrentMember(): Boolean =
        this is PlaceRecommender.Member && memberId == currentMember.id

    private fun String.toRegionCoordinate(): CourseCoordinate {
        val normalizedRegion = trim()
        return RegionCenterCoordinates.entries
            .firstOrNull { (keyword, _) -> keyword in normalizedRegion }
            ?.value
            ?: DefaultRegionCoordinate
    }

    private data class StoredCourseRoom(
        val id: String,
        var name: String,
        var region: String,
        var regionCoordinate: CourseCoordinate,
        var date: CourseDate,
        var minBudget: Int,
        var maxBudget: Int,
        val relationship: Relationship,
        val members: List<CourseMember>,
        val recommendations: MutableList<StoredRecommendation>,
        val coursePlaceIds: MutableList<String>,
        val commentsByPlaceId: MutableMap<String, MutableList<CourseComment>>,
    )

    private data class StoredRecommendation(
        val placeId: String,
        val recommender: PlaceRecommender,
        val likedByMemberIds: MutableSet<String>,
    )

    private val CourseDate.sortKey: Int
        get() = year * 10_000 + month * 100 + day

    private companion object {
        const val QueryDelayMillis = 180L
        const val ActionDelayMillis = 120L
        const val CourseCreateDelayMillis = 1200L
        const val InviteBaseUrl = "https://daytodo.app/invite/course"
        const val InviteHashRadix = 16
        val DefaultRegionCoordinate = CourseCoordinate(latitude = 37.5665, longitude = 126.9780)
        val RegionCenterCoordinates = mapOf(
            "성수" to CourseCoordinate(latitude = 37.5446, longitude = 127.0557),
            "건대" to CourseCoordinate(latitude = 37.5404, longitude = 127.0692),
            "왕십리" to CourseCoordinate(latitude = 37.5615, longitude = 127.0378),
            "용산" to CourseCoordinate(latitude = 37.5326, longitude = 126.9900),
            "이태원" to CourseCoordinate(latitude = 37.5348, longitude = 126.9944),
            "한남" to CourseCoordinate(latitude = 37.5344, longitude = 127.0107),
            "여의도" to CourseCoordinate(latitude = 37.5259, longitude = 126.9256),
            "영등포" to CourseCoordinate(latitude = 37.5172, longitude = 126.9071),
            "홍대" to CourseCoordinate(latitude = 37.5575, longitude = 126.9245),
            "연남" to CourseCoordinate(latitude = 37.5626, longitude = 126.9237),
            "망원" to CourseCoordinate(latitude = 37.5568, longitude = 126.9019),
            "강남" to CourseCoordinate(latitude = 37.4979, longitude = 127.0276),
            "서초" to CourseCoordinate(latitude = 37.4836, longitude = 127.0327),
            "종로" to CourseCoordinate(latitude = 37.5729, longitude = 126.9794),
            "을지로" to CourseCoordinate(latitude = 37.5663, longitude = 126.9919),
            "동대문" to CourseCoordinate(latitude = 37.5710, longitude = 127.0095),
        )
    }
}
