package com.team_daytodo.daytodo.feature.course.presentation.component

import android.graphics.Bitmap
import android.graphics.Canvas as AndroidCanvas
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.graphics.Typeface
import android.view.View
import android.widget.FrameLayout
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.team_daytodo.daytodo.domain.course.model.CourseCoordinate
import com.team_daytodo.daytodo.domain.course.model.CoursePlace
import com.team_daytodo.daytodo.feature.course.KakaoMapInitializer
import com.team_daytodo.daytodo.feature.course.model.MapMarkerPlace
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import kotlin.math.roundToInt

@Composable
internal fun PlaceRecommendationMap(
    markers: List<MapMarkerPlace>,
    selectedPlaceId: String?,
    regionCoordinate: CourseCoordinate?,
    bottomMapPadding: Dp,
    onMarkerClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val density = LocalDensity.current
    val supportsKakaoMap = remember { KakaoMapInitializer.isSupportedAbi() }
    val unsupportedMapMessage = remember(supportsKakaoMap) {
        if (supportsKakaoMap) {
            null
        } else {
            "현재 기기 ABI(${KakaoMapInitializer.primaryDeviceAbi()})는 Kakao Maps SDK 네이티브 라이브러리를 지원하지 않아 대체 지도를 표시해요.\n실제 지도는 ARM 기기에서 확인할 수 있어요."
        }
    }
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var kakaoMap by remember { mutableStateOf<KakaoMap?>(null) }
    var mapStartError by remember { mutableStateOf<String?>(null) }
    var mapHeightPx by remember { mutableStateOf(0) }
    val currentMarkers by rememberUpdatedState(markers)
    val currentOnMarkerClick by rememberUpdatedState(onMarkerClick)
    val requestedBottomMapPaddingPx = with(density) { bottomMapPadding.roundToPx() }
    val minimumVisibleMapHeightPx = with(density) { MinimumVisibleMapHeight.roundToPx() }
    val bottomMapPaddingPx = if (mapHeightPx > 0) {
        requestedBottomMapPaddingPx.coerceAtMost(
            (mapHeightPx - minimumVisibleMapHeightPx).coerceAtLeast(0),
        )
    } else {
        requestedBottomMapPaddingPx
    }
    val currentBottomMapPaddingPx by rememberUpdatedState(bottomMapPaddingPx)

    DisposableEffect(lifecycleOwner, mapView) {
        val currentMapView = mapView
        val lifecycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    currentMapView?.safeResume()
                    currentMapView?.context?.let {
                        KakaoMapInitializer.logMapLifecycle(it, "resume")
                    }
                }
                Lifecycle.Event.ON_PAUSE -> {
                    currentMapView?.safePause()
                    currentMapView?.context?.let {
                        KakaoMapInitializer.logMapLifecycle(it, "pause")
                    }
                }
                else -> Unit
            }
        }
        lifecycle.addObserver(observer)
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            currentMapView?.safeResume()
            currentMapView?.context?.let {
                KakaoMapInitializer.logMapLifecycle(it, "resume-current")
            }
        }
        onDispose {
            lifecycle.removeObserver(observer)
            currentMapView?.safePause()
            currentMapView?.context?.let {
                KakaoMapInitializer.logMapLifecycle(it, "dispose")
            }
            currentMapView?.safeFinish()
        }
    }

    LaunchedEffect(kakaoMap, markers, selectedPlaceId) {
        val readyMap = kakaoMap ?: return@LaunchedEffect
        readyMap.renderCourseMarkers(
            markers = markers,
            selectedPlaceId = selectedPlaceId,
            density = density.density,
        )
    }

    LaunchedEffect(kakaoMap, regionCoordinate, selectedPlaceId, bottomMapPaddingPx) {
        val readyMap = kakaoMap ?: return@LaunchedEffect
        readyMap.setBottomVisiblePadding(bottomMapPaddingPx)
        if (selectedPlaceId == null) {
            readyMap.moveCamera(
                CameraUpdateFactory.newCenterPosition(
                    regionCoordinate?.toLatLng() ?: DefaultMapCenter,
                    DefaultMapZoom,
                ),
                CameraAnimation.from(250, true, true),
            )
        }
    }

    LaunchedEffect(kakaoMap, selectedPlaceId, markers, bottomMapPaddingPx) {
        val readyMap = kakaoMap ?: return@LaunchedEffect
        readyMap.setBottomVisiblePadding(bottomMapPaddingPx)
        val selectedMarker = markers.firstOrNull { it.place.id == selectedPlaceId }
        if (selectedMarker != null) {
            readyMap.moveToPlace(selectedMarker.place)
        }
    }

    LaunchedEffect(kakaoMap, bottomMapPaddingPx) {
        kakaoMap?.setBottomVisiblePadding(bottomMapPaddingPx)
    }

    Box(
        modifier = modifier
            .background(Color(0xFFEAF2F4))
            .onSizeChanged { size -> mapHeightPx = size.height },
    ) {
        if (supportsKakaoMap) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { viewContext ->
                    try {
                        KakaoMapInitializer
                            .initialize(viewContext.applicationContext)
                            .getOrThrow()

                        MapView(viewContext).also { createdMapView ->
                            createdMapView.setFinishManually(true)
                            KakaoMapInitializer.logMapViewCreated(viewContext.applicationContext)
                            mapView = createdMapView
                            createdMapView.startWhenAttached(
                                object : MapLifeCycleCallback() {
                                    override fun onMapDestroy() = Unit

                                    override fun onMapError(error: Exception) {
                                        KakaoMapInitializer.logMapError(
                                            context = viewContext.applicationContext,
                                            error = error,
                                        )
                                        mapStartError = error.message
                                            ?.takeIf(String::isNotBlank)
                                            ?.let { "카카오 지도를 불러오지 못했어요.\n$it" }
                                            ?: "카카오 지도를 불러오지 못했어요. 인증/네트워크 설정을 확인해 주세요."
                                    }
                                },
                                object : KakaoMapReadyCallback() {
                                    override fun getPosition(): LatLng {
                                        return regionCoordinate?.toLatLng()
                                            ?: markers.firstOrNull()?.place?.coordinate?.toLatLng()
                                            ?: DefaultMapCenter
                                    }

                                    override fun getZoomLevel(): Int = DefaultMapZoom

                                    override fun onMapReady(map: KakaoMap) {
                                        KakaoMapInitializer.logMapReady(viewContext.applicationContext)
                                        mapStartError = null
                                        kakaoMap = map
                                        map.setOnLabelClickListener { _, _, label ->
                                            val placeId = label.tag as? String ?: return@setOnLabelClickListener false
                                            map.setBottomVisiblePadding(currentBottomMapPaddingPx)
                                            currentMarkers
                                                .firstOrNull { marker -> marker.place.id == placeId }
                                                ?.let { marker ->
                                                    map.moveToPlace(marker.place)
                                                }
                                            currentOnMarkerClick(placeId)
                                            true
                                        }
                                    }
                                },
                                shouldResumeAfterStart = {
                                    lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
                                },
                            )
                        }
                    } catch (error: UnsatisfiedLinkError) {
                        KakaoMapInitializer.logMapError(
                            context = viewContext.applicationContext,
                            error = error,
                        )
                        mapStartError = "Kakao 지도 네이티브 라이브러리를 현재 기기 ABI에서 사용할 수 없어요.\n지원 ABI: arm64-v8a, armeabi-v7a"
                        FrameLayout(viewContext)
                    } catch (error: Exception) {
                        KakaoMapInitializer.logMapError(
                            context = viewContext.applicationContext,
                            error = error,
                        )
                        mapStartError = "카카오 지도를 불러오지 못했어요. 인증/네트워크 설정을 확인해 주세요."
                        FrameLayout(viewContext)
                    }
                },
            )
        } else {
            CourseMapFallback(
                markers = markers,
                selectedPlaceId = selectedPlaceId,
                onMarkerClick = onMarkerClick,
                modifier = Modifier.fillMaxSize(),
            )
        }

        val visibleMapMessage = mapStartError ?: unsupportedMapMessage
        if (visibleMapMessage != null) {
            Text(
                text = visibleMapMessage,
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.88f))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                style = DayTodoTheme.typography.caption1.copy(letterSpacing = 0.sp),
                color = DayTodoTheme.colors.textPrimary,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun CourseMapFallback(
    markers: List<MapMarkerPlace>,
    selectedPlaceId: String?,
    onMarkerClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.background(Color(0xFFEAF2F4))) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(color = Color(0xFFEAF2F4))
            drawCircle(
                color = Color(0xFFD8E9D2),
                radius = size.minDimension * 0.28f,
                center = Offset(size.width * 0.88f, size.height * 0.16f),
            )
            drawCircle(
                color = Color(0xFFDDEDD8),
                radius = size.minDimension * 0.2f,
                center = Offset(size.width * 0.12f, size.height * 0.82f),
            )
            drawLine(
                color = Color.White.copy(alpha = 0.95f),
                start = Offset(size.width * 0.05f, size.height * 0.18f),
                end = Offset(size.width * 0.95f, size.height * 0.44f),
                strokeWidth = 22.dp.toPx(),
            )
            drawLine(
                color = Color.White.copy(alpha = 0.95f),
                start = Offset(size.width * 0.18f, 0f),
                end = Offset(size.width * 0.62f, size.height),
                strokeWidth = 18.dp.toPx(),
            )
            drawLine(
                color = Color.White.copy(alpha = 0.9f),
                start = Offset(0f, size.height * 0.64f),
                end = Offset(size.width, size.height * 0.36f),
                strokeWidth = 16.dp.toPx(),
            )
            drawLine(
                color = Color(0xFFDADCE3),
                start = Offset(size.width * 0.05f, size.height * 0.18f),
                end = Offset(size.width * 0.95f, size.height * 0.44f),
                strokeWidth = 2.dp.toPx(),
            )
            drawLine(
                color = Color(0xFFDADCE3),
                start = Offset(size.width * 0.18f, 0f),
                end = Offset(size.width * 0.62f, size.height),
                strokeWidth = 2.dp.toPx(),
            )
        }

        val markerModifiers = listOf(
            Modifier.align(Alignment.Center).offset(x = (-124).dp, y = (-84).dp),
            Modifier.align(Alignment.Center).offset(x = 74.dp, y = (-122).dp),
            Modifier.align(Alignment.Center).offset(x = 112.dp, y = (-24).dp),
            Modifier.align(Alignment.Center).offset(x = (-84).dp, y = 76.dp),
            Modifier.align(Alignment.Center).offset(x = 8.dp, y = 116.dp),
            Modifier.align(Alignment.Center).offset(x = (-12).dp, y = (-8).dp),
        )
        markers.take(markerModifiers.size).forEachIndexed { index, marker ->
            FallbackMapMarker(
                marker = marker,
                selected = marker.place.id == selectedPlaceId,
                onClick = { onMarkerClick(marker.place.id) },
                modifier = markerModifiers[index],
            )
        }
    }
}

@Composable
private fun FallbackMapMarker(
    marker: MapMarkerPlace,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .size(if (selected) 42.dp else 36.dp)
            .clickable(role = Role.Button, onClick = onClick),
        shape = CircleShape,
        color = if (selected) Color(0xFFA09FF5) else Color(0xFF8B8AF5),
        shadowElevation = if (selected) 6.dp else 2.dp,
        border = BorderStroke(2.dp, Color.White),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = marker.label.take(3),
                style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
                color = Color.White,
                maxLines = 1,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private fun KakaoMap.renderCourseMarkers(
    markers: List<MapMarkerPlace>,
    selectedPlaceId: String?,
    density: Float,
) {
    val layer = labelManager?.layer ?: return
    layer.removeAll()

    markers.forEach { marker ->
        val selected = marker.place.id == selectedPlaceId
        val markerBitmap = createKakaoMarkerBitmap(
            label = marker.label,
            selected = selected,
            density = density,
        )
        val styles = LabelStyle
            .from(markerBitmap)
            .setAnchorPoint(0.5f, 0.5f)

        layer.addLabel(
            LabelOptions
                .from(
                    marker.place.id,
                    marker.place.coordinate.toLatLng(),
                )
                .setStyles(styles)
                .setClickable(true)
                .setTag(marker.place.id),
        )
    }
}

private fun createKakaoMarkerBitmap(
    label: String,
    selected: Boolean,
    density: Float,
): Bitmap {
    val size = ((if (selected) 42 else 36) * density).roundToInt()
    val strokeWidth = (2f * density).coerceAtLeast(1f)
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = AndroidCanvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val radius = size / 2f

    paint.style = Paint.Style.FILL
    paint.color = if (selected) {
        AndroidColor.rgb(160, 159, 245)
    } else {
        AndroidColor.rgb(139, 138, 245)
    }
    canvas.drawCircle(radius, radius, radius - strokeWidth, paint)

    paint.style = Paint.Style.STROKE
    paint.strokeWidth = strokeWidth
    paint.color = AndroidColor.WHITE
    canvas.drawCircle(radius, radius, radius - strokeWidth / 2f, paint)

    paint.style = Paint.Style.FILL
    paint.color = AndroidColor.WHITE
    paint.textAlign = Paint.Align.CENTER
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    paint.textSize = if (label.length > 2) 10f * density else 12f * density

    val textY = radius - (paint.descent() + paint.ascent()) / 2f
    canvas.drawText(label.take(3), radius, textY, paint)

    return bitmap
}

private fun MapView.startWhenAttached(
    lifeCycleCallback: MapLifeCycleCallback,
    readyCallback: KakaoMapReadyCallback,
    shouldResumeAfterStart: () -> Boolean,
) {
    var started = false

    fun startOnce() {
        if (started) return
        started = true

        KakaoMapInitializer.logMapStartRequested(context)
        start(lifeCycleCallback, readyCallback)
        if (shouldResumeAfterStart()) {
            safeResume()
            KakaoMapInitializer.logMapLifecycle(context, "resume-after-start")
        }
    }

    if (isAttachedToWindow) {
        KakaoMapInitializer.logMapLifecycle(context, "already-attached")
        post { startOnce() }
        return
    }

    KakaoMapInitializer.logMapLifecycle(context, "await-attach")
    addOnAttachStateChangeListener(
        object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(view: View) {
                removeOnAttachStateChangeListener(this)
                KakaoMapInitializer.logMapLifecycle(context, "attached")
                post { startOnce() }
            }

            override fun onViewDetachedFromWindow(view: View) = Unit
        },
    )
}

private fun CourseCoordinate.toLatLng(): LatLng = LatLng.from(latitude, longitude)

private fun MapView.safeResume() {
    runCatching { resume() }
}

private fun MapView.safePause() {
    if (!isAttachedToWindow) return
    runCatching { pause() }
}

private fun MapView.safeFinish() {
    runCatching { finish() }
}

private fun KakaoMap.setBottomVisiblePadding(bottomPaddingPx: Int) {
    setPadding(0, 0, 0, bottomPaddingPx)
}

private fun KakaoMap.moveToPlace(place: CoursePlace) {
    moveCamera(
        CameraUpdateFactory.newCenterPosition(
            place.coordinate.toLatLng(),
            zoomLevel.coerceAtLeast(DefaultMapZoom),
        ),
        CameraAnimation.from(250, true, true),
    )
}

private val DefaultMapCenter: LatLng
    get() = LatLng.from(37.557527, 126.924466)

private val MinimumVisibleMapHeight = 160.dp
private const val DefaultMapZoom = 15
