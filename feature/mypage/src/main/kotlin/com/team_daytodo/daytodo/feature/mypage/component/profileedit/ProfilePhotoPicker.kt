package com.team_daytodo.daytodo.feature.mypage.component.profileedit

import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val PhotoSize = 92.dp
private val AddBadgeSize = 23.dp
private val AddIconSize = 15.dp

private val AddBadgeOffset = 2.dp

@Composable
fun ProfilePhotoPicker(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageUri: String? = null,
) {
    val imageBitmap by rememberProfileImageBitmap(imageUri)

    Box(
        modifier = modifier.clickable(
            role = Role.Button,
            onClick = onClick,
        ),
    ) {
        Box(
            modifier = Modifier
                .size(PhotoSize)
                .clip(CircleShape)
                .background(DayTodoTheme.colors.backgroundSecondary),
        ) {
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap!!,
                    contentDescription = "선택한 프로필 사진",
                    modifier = Modifier.size(PhotoSize).clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = AddBadgeOffset, y = AddBadgeOffset)
                .size(AddBadgeSize)
                .clip(CircleShape)
                .background(DayTodoTheme.colors.textPrimary),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "프로필 사진 변경",
                tint = DayTodoTheme.colors.iconOnColor,
                modifier = Modifier.size(AddIconSize),
            )
        }
    }
}

@Composable
private fun rememberProfileImageBitmap(imageUri: String?): State<ImageBitmap?> {
    val context = LocalContext.current

    return produceState<ImageBitmap?>(initialValue = null, imageUri) {
        value = withContext(Dispatchers.IO) {
            runCatching {
                val uri = imageUri?.let(Uri::parse) ?: return@runCatching null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source).asImageBitmap()
                } else {
                    context.contentResolver.openInputStream(uri)?.use { input ->
                        BitmapFactory.decodeStream(input)?.asImageBitmap()
                    }
                }
            }.getOrNull()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfilePhotoPickerPreview() {
    DayTodoTheme {
        ProfilePhotoPicker(onClick = {})
    }
}
