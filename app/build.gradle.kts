import java.util.Properties

plugins {
    alias(libs.plugins.daytodo.android.application)
    alias(libs.plugins.daytodo.android.compose)
    alias(libs.plugins.daytodo.hilt)
    alias(libs.plugins.daytodo.firebase)

}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use(::load)
    }
}

android {
    namespace = "com.team_daytodo.daytodo"

    defaultConfig {
        applicationId = "com.team_daytodo.daytodo"
        versionCode = 1
        versionName = "1.0.0"

        buildConfigField(
            type = "String",
            name = "NAVER_CLIENT_ID",
            value = localProperties
                .getProperty("NAVER_CLIENT_ID", "")
                .toBuildConfigString(),
        )
        buildConfigField(
            type = "String",
            name = "NAVER_CLIENT_SECRET",
            value = localProperties
                .getProperty("NAVER_CLIENT_SECRET", "")
                .toBuildConfigString(),
        )
        buildConfigField(
            type = "String",
            name = "NAVER_CLIENT_NAME",
            value = localProperties
                .getProperty("NAVER_CLIENT_NAME", "DayTodo")
                .toBuildConfigString(),
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":uikit"))
    implementation(project(":data"))

    implementation(project(":feature:auth"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:home"))
    implementation(project(":feature:magazine"))
    implementation(project(":feature:course"))
    implementation(project(":feature:save"))
    implementation(project(":feature:calendar"))
    implementation(project(":feature:today"))
    implementation(project(":feature:record"))
    implementation(project(":feature:mypage"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.naver.oauth)
    implementation(libs.timber)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

fun String.toBuildConfigString(): String =
    "\"${replace("\\", "\\\\").replace("\"", "\\\"")}\""
