plugins {
    alias(libs.plugins.daytodo.android.library)
    alias(libs.plugins.daytodo.hilt)
    alias(libs.plugins.daytodo.retrofit)
}

android {
    namespace = "com.team_daytodo.daytodo.data"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.timber)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
