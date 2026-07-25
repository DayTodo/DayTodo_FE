package com.team_daytodo.daytodo.feature.auth.presentation

import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.auth.model.ProfileSetupUiState
import com.team_daytodo.daytodo.feature.auth.presentation.component.AuthIntroSection
import com.team_daytodo.daytodo.uikit.component.DayTodoAuthTextField
import com.team_daytodo.daytodo.uikit.component.DayTodoCircleAddIcon
import com.team_daytodo.daytodo.uikit.component.DayTodoHeaderSection
import com.team_daytodo.daytodo.uikit.component.DayTodoNextStepButton
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.team_daytodo.daytodo.uikit.R as UIKitR

@Composable
fun ProfileSetupScreen(
    uiState: ProfileSetupUiState,
    onNicknameChange: (String) -> Unit,
    onProfileImageClick: () -> Unit,
    onBackClick: () -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DayTodoTheme.colors.backgroundDefault)
            .statusBarsPadding(),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 132.dp),
        ) {
            item {
                DayTodoHeaderSection(
                    title = "프로필 관리",
                    onBackClick = onBackClick,
                    horizontalPadding = ScreenHorizontalPadding,
                )
            }
            item {
                AuthIntroSection(
                    title = "프로필을\n설정해주세요",
                    description = "프로필을 완성해보세요",
                    modifier = Modifier
                        .padding(horizontal = ScreenHorizontalPadding)
                        .padding(top = 44.dp),
                )
            }
            item { Spacer(modifier = Modifier.height(44.dp)) }
            item {
                ProfileImageSection(
                    imageUri = uiState.profileImageUri,
                    onProfileImageClick = onProfileImageClick,
                )
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item {
                DayTodoAuthTextField(
                    label = "닉네임",
                    value = uiState.nickname,
                    onValueChange = onNicknameChange,
                    modifier = Modifier.padding(horizontal = ScreenHorizontalPadding),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(onDone = { onSubmitClick() }),
                    contentPadding = PaddingValues(horizontal = 15.dp, vertical = 18.dp),
                    labelStyle = DayTodoTheme.typography.title2,
                    labelSpacing = 4.dp,
                )
            }
        }

        DayTodoNextStepButton(
            text = "설정하기",
            enabled = uiState.canSubmit,
            allowDisabledClick = true,
            onClick = onSubmitClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = ScreenHorizontalPadding)
                .padding(bottom = BottomButtonPadding),
        )
    }
}

@Composable
private fun ProfileImageSection(
    imageUri: String?,
    onProfileImageClick: () -> Unit,
) {
    val imageBitmap by rememberProfileImageBitmap(imageUri)

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.size(96.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(92.dp)
                    .clip(CircleShape)
                    .background(DayTodoTheme.colors.backgroundSecondary),
                contentAlignment = Alignment.Center,
            ) {
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap!!,
                        contentDescription = "사용자 지정 프로필 이미지",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(
                            id = UIKitR.drawable.ic_default_profile,
                        ),
                        contentDescription = "기본 프로필 이미지",
                        modifier = Modifier.size(46.dp),
                        tint = androidx.compose.ui.graphics.Color.Unspecified,
                    )
                }
            }
            DayTodoCircleAddIcon(
                containerColor = DayTodoTheme.colors.iconDefault,
                size = 23.dp,
                iconSize = 14.dp,
                contentDescription = "프로필 사진 변경",
                onClick = onProfileImageClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 1.dp, y = 1.dp),
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "+를 눌러 프로필 사진을 변경할 수 있어요",
            style = DayTodoTheme.typography.caption2,
            color = DayTodoTheme.colors.iconDefault,
        )
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
                    val source = ImageDecoder.createSource(
                        context.contentResolver,
                        uri,
                    )
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

@Preview
@Composable
private fun ProfileSetupScreenPreview() {
    ProfileSetupScreen(
        uiState = ProfileSetupUiState(),
        onNicknameChange = {},
        onProfileImageClick = {},
        onBackClick = {},
        onSubmitClick = {},
    )
}
