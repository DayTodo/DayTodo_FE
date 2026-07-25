package com.team_daytodo.daytodo.feature.mypage.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.team_daytodo.daytodo.feature.mypage.screen.MypageScreen
import com.team_daytodo.daytodo.feature.mypage.screen.ProfileEditRoute

/**
 * 마이페이지 라우트 상수.
 * Mypage 값은 기존 :app 의 DayTodoRoute.Mypage("mypage")와 동일하게 맞춰 바텀 네비와 호환되게 함.
 */
object MypageRoute {
    const val Mypage = "mypage"
    const val ProfileEdit = "mypage/profile-edit"
}

/**
 * 마이페이지 네비게이션 그래프. :app 의 NavHost 안에서 이 확장 함수를 호출해 등록한다.
 * 마이페이지 → 프로필 관리 이동만 실제 동작하며, 나머지는 UI 전용이다.
 */
fun NavGraphBuilder.mypageNavGraph(navController: NavController) {
    composable(MypageRoute.Mypage) {
        // 알림 설정은 아직 저장소가 없어 화면 상태로만 유지한다.
        // 알림 설정 API/로컬 저장 연동 시 ViewModel 로 옮긴다. (별도 이슈)
        var notificationEnabled by rememberSaveable { mutableStateOf(false) }

        MypageScreen(
            onEditProfileClick = { navController.navigate(MypageRoute.ProfileEdit) },
            // TODO: 로그인 라우트가 생기면 백스택을 비우고 로그인 화면으로 이동시킨다.
            //  현재 :feature:auth 에 등록된 라우트가 없어 비워둔다.
            onNavigateToLogin = {},
            notificationEnabled = notificationEnabled,
            onNotificationToggle = { notificationEnabled = it },
        )
    }
    composable(MypageRoute.ProfileEdit) {
        ProfileEditRoute(
            onBackClick = { navController.popBackStack() },
        )
    }
}
