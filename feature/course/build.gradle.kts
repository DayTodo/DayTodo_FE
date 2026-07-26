import java.util.Properties

plugins {
    alias(libs.plugins.daytodo.android.feature)
    alias(libs.plugins.daytodo.hilt)
}

val localProperties = Properties().apply {
    rootProject.file("local.properties")
        .inputStream()
        .use { load(it) }
}

val kakaoNativeAppKey = localProperties.getProperty("KAKAO_NATIVE_APP_KEY")
    ?: error("KAKAO_NATIVE_APP_KEY를 local.properties에서 찾을 수 없습니다.")

android {
    namespace = "com.team_daytodo.daytodo.feature.course"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField(
            type = "String",
            name = "KAKAO_NATIVE_APP_KEY",
            value = "\"$kakaoNativeAppKey\""
        )
    }
}

dependencies {
    implementation(libs.android.kakao.maps)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
