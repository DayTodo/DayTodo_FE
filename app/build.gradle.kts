plugins {
    alias(libs.plugins.daytodo.android.application)
    alias(libs.plugins.daytodo.android.compose)
    alias(libs.plugins.daytodo.hilt)
}

android {
    namespace = "com.team_daytodo.daytodo"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.team_daytodo.daytodo"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":uikit"))
    implementation(project(":data"))

    implementation(project(":feature:auth"))
    implementation(project(":feature:home"))
    implementation(project(":feature:magazine"))
    implementation(project(":feature:course"))
    implementation(project(":feature:save"))
    implementation(project(":feature:calendar"))
    implementation(project(":feature:today"))
    implementation(project(":feature:diary"))
    implementation(project(":feature:mypage"))

    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}