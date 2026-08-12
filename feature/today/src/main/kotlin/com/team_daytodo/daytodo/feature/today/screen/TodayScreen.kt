package com.team_daytodo.daytodo.feature.today.screen

import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.R
import com.team_daytodo.daytodo.feature.today.component.CourseMemberRow
import com.team_daytodo.daytodo.feature.today.component.CoursePlaceItem
import com.team_daytodo.daytodo.feature.today.component.TodayEmptyContent
import com.team_daytodo.daytodo.feature.today.model.CourseMember
import com.team_daytodo.daytodo.feature.today.model.CoursePlace
import com.team_daytodo.daytodo.feature.today.model.TodayTab
import com.team_daytodo.daytodo.uikit.dialog.DayTodoAlertDialog
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

private val TopBarMinHeight = 48.dp

@Composable
fun TodayScreen(
    hasCourse: Boolean,
    courseName: String? = null,
    members: List<CourseMember> = emptyList(),
    places: List<CoursePlace> = emptyList(),
    onBackClick: (() -> Unit)? = null,
    onAddPlaceClick: () -> Unit = {},
    onCompleteCourseClick: () -> Unit = {},
    onDeletePlaceClick: (String) -> Unit = {},
    onReorderDragStart: () -> Unit = {},
    onReorderCommit: (List<String>) -> Unit = {},
    selectedMemoryPhotoUris: List<String> = emptyList(),
    isSavingMemoryPhotos: Boolean = false,
    onAddMemoryPhotosClick: () -> Unit = {},
    onSaveMemoryPhotosClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    startWithMemoryTab: Boolean = false,
) {
    var selectedTab by remember {
        mutableStateOf(if (startWithMemoryTab) TodayTab.MEMORY else TodayTab.COURSE)
    }

    val coursePlaces = remember(places) { places.toMutableStateList() }
    var placeIdPendingDelete by remember { mutableStateOf<String?>(null) }

    val lazyListState = rememberLazyListState()
    val reorderableState = rememberReorderableLazyListState(lazyListState) { from, to ->
        val fromIndex = coursePlaces.indexOfFirst { it.id == from.key }
        val toIndex = coursePlaces.indexOfFirst { it.id == to.key }
        if (fromIndex != -1 && toIndex != -1) {
            coursePlaces.add(toIndex, coursePlaces.removeAt(fromIndex))
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DayTodoTheme.colors.backgroundDefault,
        topBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = TopBarMinHeight)
                ) {
                    if (onBackClick != null) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.align(Alignment.CenterStart),
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_back_ios),
                                contentDescription = "뒤로가기",
                            )
                        }
                    }
                    Text(
                        text = if (selectedTab == TodayTab.MEMORY) "추억 저장하기" else "투데이 코스",
                        style = DayTodoTheme.typography.title1,
                        color = DayTodoTheme.colors.textPrimary,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFFE0E0E0),
                )
            }
        },

    ) { innerPadding ->
        if (!hasCourse) {
            TodayEmptyContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 18.dp),
            )
            return@Scaffold
        }

        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 150.dp),
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    CourseMemoryToggle(
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it },
                    )
                }
            }

            if (selectedTab == TodayTab.COURSE) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(color = DayTodoTheme.colors.backgroundSecondary)
                            .padding(16.dp),
                    ) {
                        Column {
                            Text(
                                text = "${courseName.orEmpty()} 코스 멤버",
                                style = DayTodoTheme.typography.label3,
                                color = DayTodoTheme.colors.textPrimary,
                            )
                            CourseMemberRow(
                                members = members,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "코스",
                        style = DayTodoTheme.typography.label2,
                        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                    )
                }

                itemsIndexed(
                    items = coursePlaces,
                    key = { _, place -> place.id },
                ) { index, place ->
                    ReorderableItem(reorderableState, key = place.id) { isDragging ->
                        CoursePlaceItem(
                            order = index + 1,
                            place = place,
                            modifier = if (index == coursePlaces.lastIndex) {
                                Modifier
                            } else {
                                Modifier.padding(bottom = 10.dp)
                            },
                            isDragging = isDragging,
                            dragHandleModifier = Modifier.draggableHandle(
                                onDragStarted = { onReorderDragStart() },
                                onDragStopped = {
                                    onReorderCommit(coursePlaces.map { place -> place.id })
                                },
                            ),
                            onDeleteClick = { placeId -> placeIdPendingDelete = placeId },
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        Row(
                            modifier = Modifier.clickable(onClick = onAddPlaceClick),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = com.team_daytodo.daytodo.feature.today.R.drawable.ic_plus,
                                ),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.Unspecified,
                            )
                            Text(
                                text = "장소 추가",
                                style = DayTodoTheme.typography.label2,
                                color = DayTodoTheme.colors.brandPrimary,
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        Row(
                            modifier = Modifier.clickable(onClick = onAddPlaceClick),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = com.team_daytodo.daytodo.feature.today.R.drawable.ic_plus,
                                ),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.Unspecified,
                            )
                            Text(
                                text = "코스 추가",
                                style = DayTodoTheme.typography.label2,
                                color = DayTodoTheme.colors.brandPrimary,
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 56.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Row(
                            modifier = Modifier
                                .width(100.dp)
                                .height(50.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = DayTodoTheme.colors.brandPrimary)
                                .clickable(onClick = onCompleteCourseClick),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "코스 종료하기",
                                style = DayTodoTheme.typography.label2,
                                color = DayTodoTheme.colors.textQuaternary
                            )
                        }
                    }
                }
            } else {
                item {
                    MemoryPhotoGrid(
                        selectedPhotoUris = selectedMemoryPhotoUris,
                        onAddClick = onAddMemoryPhotosClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                    )
                }

                item {
                    val canSave = selectedMemoryPhotoUris.isNotEmpty() && !isSavingMemoryPhotos
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Row(
                            modifier = Modifier
                                .width(100.dp)
                                .height(50.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    color = if (canSave) {
                                        DayTodoTheme.colors.brandPrimary
                                    } else {
                                        DayTodoTheme.colors.backgroundSecondary
                                    },
                                )
                                .clickable(enabled = canSave, onClick = onSaveMemoryPhotosClick),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = if (isSavingMemoryPhotos) "저장 중" else "저장하기",
                                style = DayTodoTheme.typography.label2,
                                color = if (canSave) {
                                    DayTodoTheme.colors.textQuaternary
                                } else {
                                    DayTodoTheme.colors.textTertiary
                                },
                            )
                        }
                    }
                }
            }
        }
    }

    val pendingDeletePlaceId = placeIdPendingDelete
    if (pendingDeletePlaceId != null) {
        DayTodoAlertDialog(
            title = "장소 삭제",
            message = "이 장소를 삭제할까요?",
            onConfirm = {
                onDeletePlaceClick(pendingDeletePlaceId)
                placeIdPendingDelete = null
            },
            onDismiss = { placeIdPendingDelete = null },
        )
    }
}

@Composable
private fun MemoryPhotoGrid(
    selectedPhotoUris: List<String>,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val columns = 3
    val spacing = 6.dp
    val photoCount = selectedPhotoUris.size

    BoxWithConstraints(modifier = modifier) {
        val itemSize = (maxWidth - spacing * (columns - 1)) / columns
        val rowCount = (photoCount + 1 + columns - 1) / columns
        val gridHeight = itemSize * rowCount + spacing * (rowCount - 1)

        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = Modifier
                .fillMaxWidth()
                .height(gridHeight),
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalArrangement = Arrangement.spacedBy(spacing),
            userScrollEnabled = false,
        ) {
            item {
                Box(
                    modifier = Modifier
                        .size(itemSize)
                        .background(color = DayTodoTheme.colors.backgroundSecondary)
                        .clickable(onClick = onAddClick),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "사진 추가",
                        tint = DayTodoTheme.colors.iconDefault,
                    )
                }
            }

            items(selectedPhotoUris) { uri ->
                MemoryPhotoThumbnail(
                    uri = uri,
                    modifier = Modifier.size(itemSize),
                )
            }
        }
    }
}

@Composable
private fun MemoryPhotoThumbnail(
    uri: String,
    modifier: Modifier = Modifier,
) {
    val bitmap by rememberMemoryPhotoBitmap(uri)

    Box(
        modifier = modifier.background(color = DayTodoTheme.colors.backgroundSecondary),
    ) {
        bitmap?.let {
            Image(
                bitmap = it,
                contentDescription = "선택한 추억 사진",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Composable
private fun rememberMemoryPhotoBitmap(uri: String): State<ImageBitmap?> {
    val context = LocalContext.current

    return produceState<ImageBitmap?>(initialValue = null, uri) {
        value = withContext(Dispatchers.IO) {
            runCatching {
                val parsed = Uri.parse(uri)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(context.contentResolver, parsed)
                    ImageDecoder.decodeBitmap(source).asImageBitmap()
                } else {
                    context.contentResolver.openInputStream(parsed)?.use { input ->
                        BitmapFactory.decodeStream(input)?.asImageBitmap()
                    }
                }
            }.getOrNull()
        }
    }
}

@Composable
private fun CourseMemoryToggle(
    selectedTab: TodayTab,
    onTabSelected: (TodayTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(
                // uikit에 해당 토큰이 없어 하드코딩
                color = Color(0xFFECECFF),
                shape = RoundedCornerShape(24.dp),
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        TodayTab.entries.forEach { tab ->
            val selected = tab == selectedTab
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        color = if (selected) DayTodoTheme.colors.brandPrimary else Color.Transparent,
                    )
                    .clickable { onTabSelected(tab) }
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = tab.label,
                    style = DayTodoTheme.typography.label3,
                    color = if (selected) Color.White else DayTodoTheme.colors.textPrimary,
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun TodayScreenEmptyPreview() {
    DayTodoTheme {
        TodayScreen(
            hasCourse = false,
        )
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun TodayScreenWithCoursePreview() {
    DayTodoTheme {
        TodayScreen(
            hasCourse = true,
            members = listOf(
                CourseMember(id = "1", name = "나"),
                CourseMember(id = "2", name = "수아"),
            ),
            places = listOf(
                CoursePlace(id = "1", name = "성수 카페거리", category = "카페"),
                CoursePlace(id = "2", name = "서울숲", category = "공원"),
                CoursePlace(id = "3", name = "레스토랑 예약", category = "맛집"),
            ),
        )
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun TodayScreenMemoryTabPreview() {
    DayTodoTheme {
        TodayScreen(
            hasCourse = true,
            startWithMemoryTab = true,
            members = listOf(
                CourseMember(id = "1", name = "나"),
                CourseMember(id = "2", name = "수아"),
            ),
            places = listOf(
                CoursePlace(id = "1", name = "성수 카페거리", category = "카페"),
                CoursePlace(id = "2", name = "서울숲", category = "공원"),
                CoursePlace(id = "3", name = "레스토랑 예약", category = "맛집"),
            ),
        )
    }
}

// TODO: 확인용 임시 Preview, 확인 후 삭제
// 상태 1 — 오늘 코스가 없는 상태
@Preview(showBackground = true, heightDp = 800)
@Composable
private fun TempTodayScreenNoCoursePreview() {
    DayTodoTheme {
        TodayScreen(
            hasCourse = false,
        )
    }
}

// TODO: 확인용 임시 Preview, 확인 후 삭제
// 상태 2 — 오늘 코스가 있고 장소가 여러 개(5개) 있는 정상 상태
@Preview(showBackground = true, heightDp = 900)
@Composable
private fun TempTodayScreenWithPlacesPreview() {
    DayTodoTheme {
        TodayScreen(
            hasCourse = true,
            courseName = "홍대 데이트 코스",
            members = listOf(
                CourseMember(id = "1", name = "나"),
                CourseMember(id = "2", name = "수아"),
            ),
            places = listOf(
                CoursePlace(id = "1", name = "성수 카페거리", category = "카페"),
                CoursePlace(id = "2", name = "서울숲", category = "공원"),
                CoursePlace(id = "3", name = "레스토랑 예약", category = "맛집"),
                CoursePlace(id = "4", name = "한강 피크닉", category = "야외"),
                CoursePlace(id = "5", name = "노포 술집", category = "술집"),
            ),
        )
    }
}

// TODO: 확인용 임시 Preview, 확인 후 삭제
// 상태 3 — 장소 삭제 확인 다이얼로그가 떠 있는 상태.
// TodayScreen의 다이얼로그는 내부 private 상태(placeIdPendingDelete)로만 열려서 TodayScreen
// 파라미터로는 강제로 띄울 수 없다. 실제 호출부(TodayScreen.kt)와 동일한 title/message로
// 하위 컴포넌트 DayTodoAlertDialog를 직접 사용해 같은 모습을 재현한다.
@Preview(showBackground = true)
@Composable
private fun TempTodayScreenDeleteConfirmDialogPreview() {
    DayTodoTheme {
        DayTodoAlertDialog(
            title = "장소 삭제",
            message = "이 장소를 삭제할까요?",
            onConfirm = {},
            onDismiss = {},
        )
    }
}

// TODO: 확인용 임시 Preview, 확인 후 삭제
// 상태 4 — 코스 종료 완료 후 상태.
// 코스 종료 성공 시 별도의 "완료" 전용 화면은 없고, ViewModel이 투데이 코스를 다시 조회해
// (BE가 더 이상 오늘 코스를 내려주지 않으므로) hasCourse=false인 빈 상태로 되돌아간다.
// 그래서 이 Preview는 빈 상태와 동일한 파라미터를 사용한다.
@Preview(showBackground = true, heightDp = 800)
@Composable
private fun TempTodayScreenAfterCompleteCoursePreview() {
    DayTodoTheme {
        TodayScreen(
            hasCourse = false,
        )
    }
}

// TODO: 확인용 임시 Preview, 확인 후 삭제
// 상태 5 — API 실패 시 Toast가 뜨는 상태.
// 실패 Toast는 TodayRoute가 viewModel.event(TodayEvent.ShowMessage)를 구독해 띄우는 시스템
// 오버레이라 @Preview 렌더링(실제 Activity/Window 없음)에는 나타나지 않는다. errorMessage/이벤트는
// TodayUiState/TodayViewModel에만 있고 TodayScreen 파라미터로 노출되지 않으므로, 실패해도 화면
// 자체는 직전 상태(장소 목록 등)를 그대로 유지한다는 점만 이 UiState로 재현한다.
@Preview(showBackground = true, heightDp = 800)
@Composable
private fun TempTodayScreenApiFailurePreview() {
    DayTodoTheme {
        TodayScreen(
            hasCourse = true,
            courseName = "홍대 데이트 코스",
            members = listOf(
                CourseMember(id = "1", name = "나"),
                CourseMember(id = "2", name = "수아"),
            ),
            places = listOf(
                CoursePlace(id = "1", name = "성수 카페거리", category = "카페"),
                CoursePlace(id = "2", name = "서울숲", category = "공원"),
            ),
        )
    }
}

// TODO: 확인용 임시 Preview, 확인 후 삭제
// 상태 6 — 추억사진 저장 화면(선택된 사진이 있고 저장 중인 상태)
@Preview(showBackground = true, heightDp = 800)
@Composable
private fun TempTodayScreenSavingMemoryPhotosPreview() {
    DayTodoTheme {
        TodayScreen(
            hasCourse = true,
            startWithMemoryTab = true,
            selectedMemoryPhotoUris = listOf(
                "content://media/external/images/media/1001",
                "content://media/external/images/media/1002",
                "content://media/external/images/media/1003",
            ),
            isSavingMemoryPhotos = true,
        )
    }
}
