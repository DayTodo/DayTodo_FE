import java.util.Properties

plugins {
    alias(libs.plugins.daytodo.android.library)
    alias(libs.plugins.daytodo.hilt)
    alias(libs.plugins.daytodo.retrofit)
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use(::load)
    }
}

android {
    namespace = "com.team_daytodo.daytodo.data"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField(
            type = "String",
            name = "DAYTODO_BASE_URL",
            value = localProperties
                .getProperty("DAYTODO_BASE_URL", "https://api.daytodo.com/")
                .ensureTrailingSlash()
                .toBuildConfigString(),
        )
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.timber)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}

fun String.ensureTrailingSlash(): String =
    if (endsWith("/")) this else "$this/"

fun String.toBuildConfigString(): String =
    "\"${replace("\\", "\\\\").replace("\"", "\\\"")}\""
