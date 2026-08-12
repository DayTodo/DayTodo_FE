package com.team_daytodo.daytodo.feature.home.presentation.component

import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
internal fun RemoteImage(
    imageUrl: String?,
    @DrawableRes fallbackResId: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    var imageBitmap by remember(imageUrl) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(imageUrl) {
        imageBitmap = null
        if (imageUrl.isNullOrBlank()) return@LaunchedEffect

        imageBitmap = withContext(Dispatchers.IO) {
            runCatching {
                val connection = URL(imageUrl).openConnection().apply {
                    connectTimeout = ImageLoadTimeoutMillis
                    readTimeout = ImageLoadTimeoutMillis
                }
                connection.getInputStream().use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
                }
            }.getOrNull()
        }
    }

    val loadedImage = imageBitmap
    if (loadedImage == null) {
        Image(
            modifier = modifier,
            painter = painterResource(id = fallbackResId),
            contentDescription = contentDescription,
            contentScale = contentScale,
        )
    } else {
        Image(
            modifier = modifier,
            bitmap = loadedImage,
            contentDescription = contentDescription,
            contentScale = contentScale,
        )
    }
}

private const val ImageLoadTimeoutMillis = 5_000
